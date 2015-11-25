package com.stekar.apps.sports.wtatennisnow.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.loaders.ATPTournasCursorLoader;

/**
 * Created by stekar on 1/14/15.
 */
public class ATPTournasCursorBroadcastReceiver extends BroadcastReceiver {
    private ATPTournasCursorLoader mCursorLoader;

    public ATPTournasCursorBroadcastReceiver(ATPTournasCursorLoader cursorLoader) {
        mCursorLoader = cursorLoader;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction.equalsIgnoreCase(Constants.BROADCAST_SCHEDULE_CURSOR_LOADER_READY) == true ||
                intentAction.equalsIgnoreCase(Constants.BROADCAST_SCHEDULE_READY) == true) {
            mCursorLoader.onContentChanged();
        } else {
            Log.i("Cursor ready but wrong intent received. Expected Intent to be: ", "Constants.BROADCAST_NEWS_CURSOR_LOADER_READY|BROADCAST_NEWS_READY");
        }
    }
}