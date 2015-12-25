package com.stekar.apps.sports.wtatennisnow.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;

import com.stekar.apps.sports.wtatennisnow.MainActivity;
import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.constants.PrefsConstants;
import com.stekar.apps.sports.wtatennisnow.models.ATPSchedule;
import com.stekar.apps.sports.wtatennisnow.network.ATPScheduleDownloader;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by stekar on 12/25/14.
 */
public class ATPSchedulePullIntentService extends IntentService {
    public static final String ACTION_DOWNLOAD_SCHEDULE_FEED = "com.stekar.apps.sports.wtatennisnow.services.action.download";
    // The URL of the ATP News Feed featured picture RSS feed, in String format
    public static final String DOWNLOAD_SCHEDULE_FEED_URL =
            "https://docs.google.com/document/export?format=txt&confirm=no_antivirus&id=15n19h4xhfLxp6Uva3NWTU_eQnA2u6V7AcJJDnIyPLIY";

    public static final String DOWNLOAD_TOURNAS_FEED_URL =
            "https://docs.google.com/document/export?format=txt&confirm=no_antivirus&id=1k4yRP7EMGhkD1ArB9mXPbvc0k5McPz6q99sPsI4XsFA";

    public ATPSchedulePullIntentService() {
        super("ATPRankingPullIntentService");
    }

    private ArrayList<ATPSchedule> mNewTournas;
    private boolean mShouldFireNotification;
    private static final String TAG = "TENNISNOW_ATPSCHEDULEPULLINTENTSERVICE";

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_SCHEDULE_FEED.equals(action)) {
                final Uri uri = intent.getData();
                boolean manualRefresh = intent.getBooleanExtra(ContentResolver.SYNC_EXTRAS_MANUAL, false);
                handleActionDownloadScheduleFeed(uri, manualRefresh);
                sendNotificationToStatusBar();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownloadScheduleFeed(Uri uri, boolean manualRefresh) {
        if (uri == null)
        {
            throw new IllegalArgumentException("Download URI is NULL <uri>");
        }

        ATPScheduleDownloader atpScheduleDownloader = new ATPScheduleDownloader();
        try {
            mNewTournas = new ArrayList<>();
            atpScheduleDownloader.loadJsonFromNetwork(uri, mNewTournas, manualRefresh);
            if(mNewTournas.size() > 0) {
                mShouldFireNotification = true;
            } else {
                mShouldFireNotification = false;
            }

            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            Intent localIntent = new Intent();
            localIntent.setAction(Constants.BROADCAST_SCHEDULE_PARSED);
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
        boolean prefNotif = AppController.getInstance().getPrefValue(PrefsConstants.PREFS_NOTIF_TOURNAS_ENABLE, true);

        if(mShouldFireNotification == true && prefNotif == true) {
            Resources res = getResources();

            Intent intent = new Intent();
            intent.setAction(Constants.ACTION_NOTIFICATION);
            intent.putExtra(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_SCHEDULE_TYPE);
            intent.setClass(this, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

            String scheduleContentTitle = String.format(res.getString(R.string.schedule_tourna_title_notification_statusbar),
                    mNewTournas.size());
            CharSequence styledScheduleContentTitle = Html.fromHtml(scheduleContentTitle);

            Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
            inboxStyle.setBigContentTitle(styledScheduleContentTitle);
            inboxStyle.setSummaryText(res.getString(R.string.schedule_tourna_summary_text_notification_statusbar));

            String scheduleContentText = mNewTournas.get(0).getTournaName();
            String scheduleMapTileUri = mNewTournas.get(0).getTournaMapTile();

            int imgMapTileId = res.getIdentifier(scheduleMapTileUri, "drawable",
                    getPackageName());

            for(ATPSchedule sheduleItem : mNewTournas) {
                String scheduleContent = String.format(res.getString(R.string.schedule_tourna_content_notification_statusbar),
                        sheduleItem.getTournaWinner(), sheduleItem.getTournaName());
                CharSequence styledScheduleContent = Html.fromHtml(scheduleContent);
                inboxStyle.addLine(scheduleContent);
            }

            // Fetch the news cover, return default Drawable if a problem occurs
            Bitmap scheduleMapTile = BitmapFactory.decodeResource(res, imgMapTileId);

            Notification notif = new Notification.Builder(this)
                    .setNumber(mNewTournas.size())
                    .setTicker(res.getString(R.string.news_ready_title_notification_statusbar))
                    .setSmallIcon(R.drawable.ic_notif_news)
                    .setLargeIcon(scheduleMapTile)
                    .setContentTitle(styledScheduleContentTitle)
                    .setContentText(scheduleContentText)
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
