package com.stekar.apps.sports.wtatennisnow.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;

/**
 * Created by stekar on 1/19/15.
 */
public class AsyncATPSettingsClearSharedNews extends AsyncTask<Void, Void, Integer> {
    @Override
    protected Integer doInBackground(Void ...voids) {
        final String TAG = "TENNISNOW_AsyncATPSettingsClearSharedNews";
        int sharedNewsRemoved = 0;
        try {
            sharedNewsRemoved = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPNewsHelper().updateClearAtpNewsShared();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        } finally {

        }

        return sharedNewsRemoved;
    }

    protected void onPostExecute(Integer sharedNewsRemoved) {
        if(sharedNewsRemoved <= 0) {
            return;
        }

        if(sharedNewsRemoved == 1) {
            Toast.makeText(AppController.getInstance().getAppContext(),
                    String.format(AppController.getInstance().getAppContext().getResources().getString(R.string.prefs_app_news_share_clear_success),
                            String.valueOf(sharedNewsRemoved)),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AppController.getInstance().getAppContext(),
                    String.format(AppController.getInstance().getAppContext().getResources().getString(R.string.prefs_app_news_shares_clear_success),
                            String.valueOf(sharedNewsRemoved)),
                    Toast.LENGTH_LONG).show();
        }
    }
}
