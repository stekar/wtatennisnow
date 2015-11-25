package com.stekar.apps.sports.wtatennisnow.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.loaders.ATPNewsCursorLoader;

/**
 * Created by stekar_work on 1/12/15.
 */
public class ATPNewsCursorBroadcastReceiver extends BroadcastReceiver {
    private ATPNewsCursorLoader mCursorLoader;

    public ATPNewsCursorBroadcastReceiver(ATPNewsCursorLoader cursorLoader) {
        mCursorLoader = cursorLoader;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction.equalsIgnoreCase(Constants.BROADCAST_NEWS_CURSOR_LOADER_READY) == true ||
                intentAction.equalsIgnoreCase(Constants.BROADCAST_NEWS_READY) == true) {
            mCursorLoader.onContentChanged();
        } else {
            Log.i("Cursor ready but wrong intent received. Expected Intent to be: ", "Constants.BROADCAST_NEWS_CURSOR_LOADER_READY|BROADCAST_NEWS_READY");
        }
    }
}
