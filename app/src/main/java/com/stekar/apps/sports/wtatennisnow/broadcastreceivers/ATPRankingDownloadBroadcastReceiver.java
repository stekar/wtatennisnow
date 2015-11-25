package com.stekar.apps.sports.wtatennisnow.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.fragments.PlaceholderFragmentRanking;
/**
 * Created by stekar on 12/6/14.
 */
public class ATPRankingDownloadBroadcastReceiver extends BroadcastReceiver {

    public ATPRankingDownloadBroadcastReceiver(PlaceholderFragmentRanking fragmentMainIn) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (!Constants.BROADCAST_RANKING_PARSED.equals(action)) {
            Log.i("Feed ready but not for ranking. Expected Intent to be: ", "Constants.BROADCAST_RANKING_PARSED");
            return;
        }

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(AppController.getInstance().getAppContext());
        Intent localIntent = new Intent();
        localIntent.setAction(Constants.BROADCAST_RANKING_READY);
        localIntent.addCategory(Intent.CATEGORY_DEFAULT);
        localBroadcastManager.sendBroadcast(localIntent);
    }
}
