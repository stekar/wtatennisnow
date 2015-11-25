package com.stekar.apps.sports.wtatennisnow.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.network.ATPRankingDownloader;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class ATPRankingPullIntentService extends IntentService {
    public static final String ACTION_DOWNLOAD_RANKING_FEED = "com.stekar.apps.sports.wtatennisnow.services.action.download";
    // The URL of the ATP News Feed featured picture RSS feed, in String format
    public static final String DOWNLOAD_RANKING_FEED_URL =
            "https://docs.google.com/document/export?format=txt&confirm=no_antivirus&id=1ISOeLDfrUynUZyDI2zm9gDKJROIx3-zV0yRw34WqxPc";

    public ATPRankingPullIntentService() {
        super("ATPRankingPullIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_RANKING_FEED.equals(action)) {
                final Uri uri = intent.getData();
                boolean manualRefresh = intent.getBooleanExtra(ContentResolver.SYNC_EXTRAS_MANUAL, false);
                handleActionDownloadRankingFeed(uri, manualRefresh);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownloadRankingFeed(Uri uri, boolean manualRefresh) {
        if (uri == null)
        {
            throw new IllegalArgumentException("Download URI is NULL <uri>");
        }

        ATPRankingDownloader atpRankingDownloader = new ATPRankingDownloader();
        try {
            atpRankingDownloader.loadJsonFromNetwork(uri, manualRefresh);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            Intent localIntent = new Intent();
            localIntent.setAction(Constants.BROADCAST_RANKING_PARSED);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localBroadcastManager.sendBroadcast(localIntent);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException eio) {
            eio.printStackTrace();
        } finally {
        }
    }
}
