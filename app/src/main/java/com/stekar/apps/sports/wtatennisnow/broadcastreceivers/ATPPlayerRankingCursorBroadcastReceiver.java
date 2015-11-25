package com.stekar.apps.sports.wtatennisnow.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.loaders.ATPPlayerRankingCursorLoader;

/**
 * Created by stekar on 1/22/15.
 */
public class ATPPlayerRankingCursorBroadcastReceiver  extends BroadcastReceiver {
    private ATPPlayerRankingCursorLoader mCursorLoader;

    public ATPPlayerRankingCursorBroadcastReceiver(ATPPlayerRankingCursorLoader cursorLoader) {
        mCursorLoader = cursorLoader;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction.equalsIgnoreCase(Constants.BROADCAST_RANKING_CURSOR_LOADER_READY) == true ||
                intentAction.equalsIgnoreCase(Constants.BROADCAST_RANKING_READY) == true) {
            mCursorLoader.onContentChanged();
        } else {
            Log.i("Cursor ready but wrong intent received. Expected Intent to be: ", "Constants.BROADCAST_RANKING_CURSOR_LOADER_READY|BROADCAST_RANKING_READY");
        }
    }
}