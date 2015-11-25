package com.stekar.apps.sports.wtatennisnow.asynctasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.models.ATPSchedule;

/**
 * Created by stekar_work on 1/21/15.
 */
public class AsyncATPScheduleUpdateStarFlag extends AsyncTask<ATPSchedule, Void, Boolean> {
    @Override
    protected Boolean doInBackground(ATPSchedule... scheduleItems) {
        boolean isRowsUpdated = false;
        if(scheduleItems != null) {
            ATPSchedule scheduleItem = scheduleItems[0];
            isRowsUpdated = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPTournasHelper().updateTournaItemStarFlag(scheduleItem, !scheduleItem.getIsStarred());
        }
        return isRowsUpdated;
    }

    protected void onPostExecute(Boolean isRowsUpdated) {
        if(isRowsUpdated == true) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(AppController.getInstance().getAppContext());
            Intent localIntent = new Intent();

            localIntent.setAction(Constants.BROADCAST_SCHEDULE_CURSOR_LOADER_READY);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);

            localBroadcastManager.sendBroadcast(localIntent);
        }
    }
}