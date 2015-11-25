package com.stekar.apps.sports.wtatennisnow.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;

/**
 * Created by stekar on 1/27/15.
 */
public class AsyncATPNewsClearDeletedNews extends AsyncTask<Void, Void, Integer> {
    @Override
    protected Integer doInBackground(Void ...voids) {
        final String TAG = "TENNISNOW_AsyncATPSettingsClearDeletedNews";
        int deletedNewsRestored = 0;
        try {
            deletedNewsRestored = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPNewsHelper().updateClearAtpNewsDeleted();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        } finally {

        }

        return deletedNewsRestored;
    }

    protected void onPostExecute(Integer deletedNewsRestored) {
        if(deletedNewsRestored <= 0) {
            return;
        }

        if(deletedNewsRestored == 1) {
            Toast.makeText(AppController.getInstance().getAppContext(),
                    String.format(AppController.getInstance().getAppContext().getResources().getString(R.string.prefs_app_news_deleted_clear_success),
                            String.valueOf(deletedNewsRestored)),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AppController.getInstance().getAppContext(),
                    String.format(AppController.getInstance().getAppContext().getResources().getString(R.string.prefs_app_news_deleteds_clear_success),
                            String.valueOf(deletedNewsRestored)),
                    Toast.LENGTH_LONG).show();
        }
    }
}