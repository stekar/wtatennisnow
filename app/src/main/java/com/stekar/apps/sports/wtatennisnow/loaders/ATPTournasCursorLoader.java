package com.stekar.apps.sports.wtatennisnow.loaders;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

import com.stekar.apps.sports.wtatennisnow.broadcastreceivers.ATPTournasCursorBroadcastReceiver;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.fragments.PlaceholderFragmentSchedule.*;

/**
 * Created by stekar on 1/14/15.
 */
public class ATPTournasCursorLoader extends SQLiteCursorLoader {
    ATPTournasCursorBroadcastReceiver mATPTournasCursorBroadcastReceiver;
    Context mContext;
    private HeaderSelection mQueryTournasType;

    public ATPTournasCursorLoader(Context context, HeaderSelection queryTournasType) {
        super(context);
        mContext = context;
        mATPTournasCursorBroadcastReceiver = new ATPTournasCursorBroadcastReceiver(this);
        mQueryTournasType = queryTournasType;

        this.registerReceiver();
    }

    private void unregisterReceiver() {
        if (mATPTournasCursorBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mATPTournasCursorBroadcastReceiver);
            mATPTournasCursorBroadcastReceiver = null;
        }
    }

    private void registerReceiver() {
        IntentFilter statusIntentFilter = new IntentFilter();
        statusIntentFilter.addAction(Constants.BROADCAST_SCHEDULE_CURSOR_LOADER_READY);
        statusIntentFilter.addAction(Constants.BROADCAST_SCHEDULE_READY);
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                mATPTournasCursorBroadcastReceiver,
                statusIntentFilter);
    }

    @Override
    protected Cursor loadCursor() {
        if(mQueryTournasType.getIsAll() == true) {
            return AppSqlHelper.getInstance(mContext).getATPTournasHelper().queryAtpTournas(mQueryTournasType.monthPos);
        } else if(mQueryTournasType.getIsMasters() == true) {
            return AppSqlHelper.getInstance(mContext).getATPTournasHelper().queryAtpTournasMasters1000(mQueryTournasType.monthPos);
        } else if(mQueryTournasType.getIsSlam() == true) {
            return AppSqlHelper.getInstance(mContext).getATPTournasHelper().queryAtpTournasSlam(mQueryTournasType.monthPos);
        } else {
            return AppSqlHelper.getInstance(mContext).getATPTournasHelper().queryAtpTournaStarred();
        }
    }
}
