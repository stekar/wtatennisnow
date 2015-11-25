package com.stekar.apps.sports.wtatennisnow.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;

/**
 * Created by stekar on 12/6/14.
 */
public class ATPScheduleDownloadBroadcastReceiver extends BroadcastReceiver {
    public ATPScheduleDownloadBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Constants.BROADCAST_SCHEDULE_PARSED) == false) {
            Log.i("Feed ready but not for schedule. Expected Intent to be: ", "Constants.BROADCAST_SCHEDULE_READY");
            return;
        }

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(AppController.getInstance().getAppContext());
        Intent localIntent = new Intent();
        localIntent.setAction(Constants.BROADCAST_SCHEDULE_READY);
        localIntent.addCategory(Intent.CATEGORY_DEFAULT);
        localBroadcastManager.sendBroadcast(localIntent);

    }
}
