package com.stekar.apps.sports.wtatennisnow.loaders;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

import com.stekar.apps.sports.wtatennisnow.broadcastreceivers.ATPPlayerRankingCursorBroadcastReceiver;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.fragments.PlaceholderFragmentRanking;

/**
 * Created by stekar on 1/22/15.
 */
public class ATPPlayerRankingCursorLoader extends SQLiteCursorLoader {
    ATPPlayerRankingCursorBroadcastReceiver mATPPlayerRankingCursorBroadcastReceiver;
    Context mContext;
    private int mQueryRankingType;

    public ATPPlayerRankingCursorLoader(Context context, int queryRankingType) {
        super(context);
        mContext = context;
        mQueryRankingType = queryRankingType;
        mATPPlayerRankingCursorBroadcastReceiver = new ATPPlayerRankingCursorBroadcastReceiver(this);
        this.registerReceiver();
    }

    private void unregisterReceiver() {
        if (mATPPlayerRankingCursorBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mATPPlayerRankingCursorBroadcastReceiver);
            mATPPlayerRankingCursorBroadcastReceiver = null;
        }
    }

    private void registerReceiver() {
        IntentFilter statusIntentFilter = new IntentFilter();
        statusIntentFilter.addAction(Constants.BROADCAST_RANKING_CURSOR_LOADER_READY);
        statusIntentFilter.addAction(Constants.BROADCAST_RANKING_READY);
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                mATPPlayerRankingCursorBroadcastReceiver,
                statusIntentFilter);
    }

    @Override
    protected Cursor loadCursor() {
        if(mQueryRankingType == PlaceholderFragmentRanking.HEADER_SELECTION_TOP_PLAYERS) {
            return AppSqlHelper.getInstance(mContext).getATPPlayerRankingHelper().queryAtpPlayerRankings();
        } else if(mQueryRankingType == PlaceholderFragmentRanking.HEADER_SELECTION_TITLES_YTD_PLAYERS) {
            return AppSqlHelper.getInstance(mContext).getATPPlayerRankingHelper().queryAtpPlayerRankingsTitlesYtd();
        } else {
            return null;
        }
    }
}
