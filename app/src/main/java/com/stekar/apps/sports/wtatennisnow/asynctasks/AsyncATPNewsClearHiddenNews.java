package com.stekar.apps.sports.wtatennisnow.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;

/**
 * Created by stekar_work on 1/23/15.
 */
public class AsyncATPNewsClearHiddenNews extends AsyncTask<Void, Void, Integer> {
    @Override
    protected Integer doInBackground(Void ...voids) {
        final String TAG = "TENNISNOW_AsyncATPSettingsClearHiddenNews";
        int sharedNewsRemoved = 0;
        try {
            sharedNewsRemoved = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPNewsHelper().updateClearAtpNewsHidden();
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
                    String.format(AppController.getInstance().getAppContext().getResources().getString(R.string.prefs_app_news_hidden_clear_success),
                            String.valueOf(sharedNewsRemoved)),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AppController.getInstance().getAppContext(),
                    String.format(AppController.getInstance().getAppContext().getResources().getString(R.string.prefs_app_news_hiddens_clear_success),
                            String.valueOf(sharedNewsRemoved)),
                    Toast.LENGTH_LONG).show();
        }
    }
}
