package com.stekar.apps.sports.wtatennisnow.asynctasks;

import android.os.AsyncTask;
import android.preference.Preference;
import android.text.Html;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stekar on 2/15/15.
 */
public class AsyncATPNewsDeletedNewsPrefsCount extends AsyncTask<Preference, Void, Map<Preference, Long>> {
    @Override
    protected Map<Preference, Long> doInBackground(Preference ...preferenceItems) {
        final String TAG = "AsyncATPNewsDeletedNewsPrefsCount";

        Map<Preference, Long> map = new HashMap<>(2);
        long count = 0;
        try {
            if(preferenceItems != null) {
                count = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPNewsHelper().queryAtpNewsDeletedCount();
                map.put(preferenceItems[0], count);
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        } finally {

        }

        return map;
    }

    protected void onPostExecute(Map<Preference, Long> map) {
        Preference pref = null;
        long count = 0;
        for(Map.Entry<Preference, Long> entry : map.entrySet()) {
            count = entry.getValue();
            if(count > 0) {
                pref = entry.getKey();
            }
        }

        if(pref != null) {
            String summaryText = String.format(AppController.getInstance().getResources()
                    .getString(R.string.prefs_app_news_deleted_clear_summary), count);
            CharSequence styledSummaryText = Html.fromHtml(summaryText);
            pref.setSummary(styledSummaryText);
        }
    }
}