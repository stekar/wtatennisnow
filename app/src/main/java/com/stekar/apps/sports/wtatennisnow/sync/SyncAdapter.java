/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stekar.apps.sports.wtatennisnow.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.services.ATPNewsPullIntentService;
import com.stekar.apps.sports.wtatennisnow.services.ATPRankingPullIntentService;
import com.stekar.apps.sports.wtatennisnow.services.ATPSchedulePullIntentService;


/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "TENNISNOW_SYNC_ADAPTER";
    private Context mAppContext;
    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;


    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAppContext = context;
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link android.content.AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        try {
            try {
                Log.i(TAG, "Streaming data from network: ");

                boolean manualRefresh = extras.getBoolean(ContentResolver.SYNC_EXTRAS_MANUAL);
                startATPRankingService(manualRefresh);
                startATPNewsService(manualRefresh);
                startATPScheduleService(manualRefresh);
            } finally {

            }
        } catch (Exception e) {
            Log.wtf(TAG, "Sync Exception", e);
            syncResult.stats.numSkippedEntries++;
            return;
        /*catch (MalformedURLException e) {
            Log.wtf(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }*/
        }
        Log.i(TAG, "Network synchronization complete");
    }

    private void startATPRankingService(boolean manualRefresh) {
        Intent mServiceIntent = new Intent(mAppContext, ATPRankingPullIntentService.class);
        mServiceIntent.putExtra(ContentResolver.SYNC_EXTRAS_MANUAL, manualRefresh);
        mServiceIntent.setData(Uri.parse(ATPRankingPullIntentService.DOWNLOAD_RANKING_FEED_URL));
        mServiceIntent.setAction(ATPRankingPullIntentService.ACTION_DOWNLOAD_RANKING_FEED);

        mAppContext.startService(mServiceIntent);
    }

    private void startATPNewsService(boolean manualRefresh) {
        Intent mServiceIntent = new Intent(mAppContext, ATPNewsPullIntentService.class);
        mServiceIntent.putExtra(ContentResolver.SYNC_EXTRAS_MANUAL, manualRefresh);
        mServiceIntent.setData(Uri.parse(ATPNewsPullIntentService.DOWNLOAD_NEWS_FEED_URL));
        mServiceIntent.setAction(ATPNewsPullIntentService.ACTION_DOWNLOAD_NEWS_FEED);

        mAppContext.startService(mServiceIntent);
    }

    private void startATPScheduleService(boolean manualRefresh) {
        Intent mServiceIntent = new Intent(mAppContext, ATPSchedulePullIntentService.class);
        mServiceIntent.putExtra(ContentResolver.SYNC_EXTRAS_MANUAL, manualRefresh);
        mServiceIntent.setData(Uri.parse(ATPSchedulePullIntentService.DOWNLOAD_TOURNAS_FEED_URL));
        mServiceIntent.setAction(ATPSchedulePullIntentService.ACTION_DOWNLOAD_SCHEDULE_FEED);

        mAppContext.startService(mServiceIntent);
    }
}
