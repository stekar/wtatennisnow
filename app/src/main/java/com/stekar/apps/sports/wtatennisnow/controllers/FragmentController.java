package com.stekar.apps.sports.wtatennisnow.controllers;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.broadcastreceivers.ATPNewsDownloadBroadcastReceiver;
import com.stekar.apps.sports.wtatennisnow.broadcastreceivers.ATPRankingDownloadBroadcastReceiver;
import com.stekar.apps.sports.wtatennisnow.broadcastreceivers.ATPScheduleDownloadBroadcastReceiver;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.fragments.*;
import com.stekar.apps.sports.wtatennisnow.sync.SyncUtils;

/**
 * Created by stekar_work on 12/9/14.
 */
public class FragmentController {
    private final String TAG = "TENNISNOW_FRAGMENT_CONTROLLER";
    private Context mContext;
    private Fragment mCurFragment;
    private int mFragmentType;
    private ATPNewsDownloadBroadcastReceiver mAtpNewsDownloadBroadcastReceiver;
    private ATPRankingDownloadBroadcastReceiver mAtpRankingDownloadBroadcastReceiver;
    private ATPScheduleDownloadBroadcastReceiver mAtpScheduleDownloadBroadcastReceiver;

    public FragmentController(Context ctx, Fragment fragment, int type) {
        this.mContext = ctx;
        this.mCurFragment = fragment;
        this.mFragmentType = type;

        SyncUtils.CreateSyncAccount(this.mContext);
    }

    public void setCurFragment(Fragment newFragment) {
        this.mCurFragment = newFragment;
    }

    public void setFragmentType(int newType) {
        this.mFragmentType = newType;
    }

    public int getFragmentType() {
        return mFragmentType;
    }

    public void fetchData() {
        if(this.mCurFragment == null) {
            throw new IllegalArgumentException("CurrentFragment is null");
        }

        if(this.mFragmentType == Constants.FRAGMENT_NEWS_TYPE) {
            ((PlaceholderFragmentNews) this.mCurFragment).fetchNewsData();
        } else if(this.mFragmentType == Constants.FRAGMENT_RANKING_TYPE){
            ((PlaceholderFragmentRanking) this.mCurFragment).fetchRankingData();
        } else {
            ((PlaceholderFragmentSchedule) this.mCurFragment).fetchScheduleData();
        }
    }
    public void registerBroadcastReceivers() {
        if(this.mFragmentType == Constants.FRAGMENT_NEWS_TYPE) {
            // Instantiates a new DownloadStateReceiver
            mAtpNewsDownloadBroadcastReceiver = new ATPNewsDownloadBroadcastReceiver();

            // The filter's action is BROADCAST_ACTION
            IntentFilter statusIntentFilter = new IntentFilter(
                    Constants.BROADCAST_NEWS_PARSED);
            // Sets the filter's category to DEFAULT
            statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

            // Registers the DownloadStateReceiver and its intent filters
            LocalBroadcastManager.getInstance(this.mContext).registerReceiver(
                    mAtpNewsDownloadBroadcastReceiver,
                    statusIntentFilter);
        } else if(this.mFragmentType == Constants.FRAGMENT_RANKING_TYPE) {
            // Instantiates a new DownloadStateReceiver
            mAtpRankingDownloadBroadcastReceiver = new ATPRankingDownloadBroadcastReceiver((PlaceholderFragmentRanking) this.mCurFragment);

            // The filter's action is BROADCAST_ACTION
            IntentFilter statusIntentFilter = new IntentFilter(
                    Constants.BROADCAST_RANKING_PARSED);
            // Sets the filter's category to DEFAULT
            statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

            // Registers the DownloadStateReceiver and its intent filters
            LocalBroadcastManager.getInstance(this.mContext).registerReceiver(
                    mAtpRankingDownloadBroadcastReceiver,
                    statusIntentFilter);
        } else if(this.mFragmentType == Constants.FRAGMENT_SCHEDULE_TYPE) {
            // Instantiates a new DownloadStateReceiver
            mAtpScheduleDownloadBroadcastReceiver = new ATPScheduleDownloadBroadcastReceiver();

            // The filter's action is BROADCAST_ACTION
            IntentFilter statusIntentFilter = new IntentFilter(
                    Constants.BROADCAST_SCHEDULE_PARSED);
            // Sets the filter's category to DEFAULT
            statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

            // Registers the DownloadStateReceiver and its intent filters
            LocalBroadcastManager.getInstance(this.mContext).registerReceiver(
                    mAtpScheduleDownloadBroadcastReceiver,
                    statusIntentFilter);
        } else {
            Log.v(TAG, "registerBroadcastReceivers :: Fragment type does not exist.");
        }
    }

    public void unregisterBroadcastReceivers() {
        if(this.mFragmentType == Constants.FRAGMENT_NEWS_TYPE) {
            // If the ATPNewsDownloadBroadcastReceiver still exists, unregister it and set it to null
            if (mAtpNewsDownloadBroadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(mAtpNewsDownloadBroadcastReceiver);
                mAtpNewsDownloadBroadcastReceiver = null;
            }
        } else  if(this.mFragmentType == Constants.FRAGMENT_RANKING_TYPE) {
            if (mAtpRankingDownloadBroadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(mAtpRankingDownloadBroadcastReceiver);
                mAtpRankingDownloadBroadcastReceiver = null;
            }
        } else  if(this.mFragmentType == Constants.FRAGMENT_SCHEDULE_TYPE) {
            if (mAtpScheduleDownloadBroadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(mAtpScheduleDownloadBroadcastReceiver);
                mAtpScheduleDownloadBroadcastReceiver = null;
            }
        } else {
            Log.v(TAG, "unregisterBroadcastReceivers :: Fragment type does not exist.");
        }
    }
}
