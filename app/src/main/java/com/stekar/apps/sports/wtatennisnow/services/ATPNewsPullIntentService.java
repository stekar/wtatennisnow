package com.stekar.apps.sports.wtatennisnow.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.MainActivity;
import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.constants.PrefsConstants;
import com.stekar.apps.sports.wtatennisnow.models.ATPNews;
import com.stekar.apps.sports.wtatennisnow.network.NewsDownloader;
import com.stekar.apps.sports.wtatennisnow.utils.BitmapUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class ATPNewsPullIntentService extends IntentService {
    private boolean mShouldFireNotification;
    private boolean mIsManualRefresh;
    private List<ATPNews> mNewNews;
    private static final String TAG = "WTATENNISNOW_ATPNNEWSPULLINTENTSERVICE";

    public static final String ACTION_DOWNLOAD_NEWS_FEED = "com.stekar.apps.sports.wtatennisnow.services.action.download";
    // The URL of the ATP News Feed featured picture RSS feed, in String format
    public static final String DOWNLOAD_NEWS_FEED_URL =
            "http://www.wtatennis.com/feed.rss/category/12009823,12010172/limit/25/page/news/request/article";

    public ATPNewsPullIntentService() {
        super("ATPNewsPullIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_NEWS_FEED.equals(action)) {
                final Uri uri = intent.getData();
                mIsManualRefresh = intent.getBooleanExtra(ContentResolver.SYNC_EXTRAS_MANUAL, false);
                handleActionDownloadNewsFeed(uri);
                sendNotificationToStatusBar();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownloadNewsFeed(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Download URI is NULL <uri>");
        }

        NewsDownloader newsDownloader = new NewsDownloader();
        try {
            mNewNews = new ArrayList<>();

            // Parse and store news into the database
            boolean areNewNews = newsDownloader.loadXmlFromNetwork(uri, mNewNews, mIsManualRefresh);
            boolean prefNotif = AppController.getInstance().getPrefValue(PrefsConstants.PREFS_NOTIF_NEWS_ENABLE, true);
            mShouldFireNotification = (mIsManualRefresh == false) &&
                    (areNewNews == true) &&
                    (prefNotif == true);

            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            Intent localIntent = new Intent();
            localIntent.setAction(Constants.BROADCAST_NEWS_PARSED);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localBroadcastManager.sendBroadcast(localIntent);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException eio) {
            eio.printStackTrace();
        } finally {
        }
    }

    // Inform the user of updated news, for non-manual refresh use case
    private void sendNotificationToStatusBar() {
        if(mShouldFireNotification == true) {
            Resources res = getResources();

            Intent intent = new Intent();
            intent.setAction(Constants.ACTION_NOTIFICATION);
            intent.putExtra(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_NEWS_TYPE);
            intent.setClass(this, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

            String newsContentTitle = String.format(res.getString(R.string.news_ready_title_notification_statusbar),
                    mNewNews.size());
            CharSequence styledNewsContentTitle = Html.fromHtml(newsContentTitle);

            Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
            inboxStyle.setBigContentTitle(styledNewsContentTitle);
            inboxStyle.setSummaryText(res.getString(R.string.news_ready_summary_text_notification_statusbar));

            String newsContentText = mNewNews.get(0).getTitle();
            String newsCoverUrl = null;

            for(ATPNews newsItem : mNewNews) {
                inboxStyle.addLine(newsItem.getTitle());

                // Stop when the cover URL is found
                if(newsCoverUrl == null) {
                    newsCoverUrl = newsItem.getCover();
                    if(newsCoverUrl != null) {
                        Log.d(TAG, "Found news cover url <" + newsCoverUrl + "> for news title [" + newsItem.getTitle() + "]");
                    }
                }
            }

            // Fetch the news cover, return default Drawable if a problem occurs
            Bitmap newsCover = BitmapUtils.loadFrom(newsCoverUrl, this, R.drawable.ic_app);

            Notification notif = new Notification.Builder(this)
                    .setNumber(mNewNews.size())
                    .setTicker(res.getString(R.string.news_ready_title_notification_statusbar))
                    .setSmallIcon(R.drawable.ic_notif_news)
                    .setLargeIcon(newsCover)
                    .setContentTitle(styledNewsContentTitle)
                    .setContentText(newsContentText)
                    .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setStyle(inboxStyle)
                    .setCategory(Notification.CATEGORY_RECOMMENDATION)
                    .build();

            NotificationManager notifMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notifMgr.notify(0, notif);
        }
    }
}
