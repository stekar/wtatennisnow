package com.stekar.apps.sports.wtatennisnow.asynctasks;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;
import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.cursors.ATPTournasCursor;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.models.ATPSchedule;

/**
 * Created by stekar on 1/19/15.
 */
public class AsyncATPSettingsClearEvents extends AsyncTask<Void, Void, Integer> {
    private Context mContext;

    @Override
    protected Integer doInBackground(Void ...voids) {
        final String TAG = "TENNISNOW_AsyncATPSettingsClearEvents";
        ATPTournasCursor cursor = null;
        int eventsRemoved = 0;
        try {
            cursor = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPTournasHelper().queryAtpTournasWithEvents();
            ATPSchedule scheduleItem;
            while (cursor.moveToNext()) {
                scheduleItem = cursor.getScheduleItem();
                if(removeTournaFromCalendar(scheduleItem) == true) {
                    eventsRemoved++;
                } else {
                    eventsRemoved--;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return eventsRemoved;
    }

    protected void onPostExecute(Integer eventsRemoved) {
        if(eventsRemoved <= 0) {
            return;
        }

        if(eventsRemoved == 1) {
            Toast.makeText(AppController.getInstance().getAppContext(),
                    String.format(AppController.getInstance().getAppContext().getResources().getString(R.string.prefs_app_tourna_event_clear_success),
                            String.valueOf(eventsRemoved)),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AppController.getInstance().getAppContext(),
                    String.format(AppController.getInstance().getAppContext().getResources().getString(R.string.prefs_app_tourna_events_clear_success),
                            String.valueOf(eventsRemoved)),
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean removeTournaFromCalendar(ATPSchedule scheduleItem) {
        ContentResolver contentResolver = AppController.getInstance().getAppContext().getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, scheduleItem.getTournaEventId());
        int rows = contentResolver.delete(deleteUri, null, null);

        // This is to account for the user removing the event from within the calendar App
        // instead of within the App.
        // Int hat case, we (blindly) assume we are in the 'add' mode
        if(rows > 0) {
            scheduleItem.setTournaEventId(0);
            return AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPTournasHelper().updateTournaItemEventFlag(scheduleItem,
                    scheduleItem.getTournaEventId());
        } else {
            return false;
        }
    }
}