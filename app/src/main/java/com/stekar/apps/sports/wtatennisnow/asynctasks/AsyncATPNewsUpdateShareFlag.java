package com.stekar.apps.sports.wtatennisnow.asynctasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.models.ATPNews;

/**
 * Created by stekar_work on 1/13/15.
 */
public class AsyncATPNewsUpdateShareFlag extends AsyncTask<ATPNews, Void, Boolean> {
    @Override
    protected Boolean doInBackground(ATPNews... newsItems) {
        boolean isRowsUpdated = false;
        if(newsItems != null) {
            ATPNews newsItem = newsItems[0];
            isRowsUpdated = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPNewsHelper().updateNewsItemShareFlag(newsItem, !newsItem.getIsShared());
        }
        return isRowsUpdated;
    }

    protected void onPostExecute(Boolean isRowsUpdated) {
        if(isRowsUpdated == true) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(AppController.getInstance().getAppContext());
            Intent localIntent = new Intent();

            localIntent.setAction(Constants.BROADCAST_NEWS_CURSOR_LOADER_READY);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);

            localBroadcastManager.sendBroadcast(localIntent);
        }
    }
}