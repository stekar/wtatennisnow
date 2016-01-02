package com.stekar.apps.sports.wtatennisnow.app;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.stekar.apps.sports.wtatennisnow.constants.PrefsConstants;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.fragments.SettingsFragment;
import com.stekar.apps.sports.wtatennisnow.utils.LruBitmapCache;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static SharedPreferences mPrefs;
    private AppSqlHelper mAppSqlHelper;
    private static AppController mInstance;
    private static Context mContext;
    private static String mPackageName;
    private ExecutorService mExecService;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        mPackageName = getPackageName();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.initExecutorService();
    }

    private void initExecutorService() {
        mExecService = Executors.newSingleThreadExecutor();
        mExecService.execute(new Runnable() {
            @Override
            public void run() {
                AppSqlHelper.getInstance(mContext);
                initSharedPrefs();
                initCalendar();
            }
        });
        mExecService.shutdown();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    private static void initSharedPrefs() {
        SharedPreferences.Editor editor = mPrefs.edit();

        long newsLastFetch = getInstance().getPrefValue(PrefsConstants.PREFS_NEWS_LAST_FETCH_MILLIS, 0L);
        long rankLastFetch = getInstance().getPrefValue(PrefsConstants.PREFS_RANKING_LAST_FETCH_MILLIS, 0L);
        long tournaLastFetch = getInstance().getPrefValue(PrefsConstants.PREFS_SCHEDULE_LAST_FETCH_MILLIS, 0L);
        long calId = getInstance().getPrefValue(PrefsConstants.PREFS_CALENDAR_DEFAULT_ID, 1L);

        editor.putLong(PrefsConstants.PREFS_NEWS_LAST_FETCH_MILLIS, newsLastFetch);
        editor.putLong(PrefsConstants.PREFS_NEWS_FETCH_REFRESH_INTERVAL, (2*3600000)); // 2-hour
        editor.putLong(PrefsConstants.PREFS_RANKING_LAST_FETCH_MILLIS, rankLastFetch);
        editor.putLong(PrefsConstants.PREFS_RANKING_FETCH_REFRESH_INTERVAL, (3*3600000)); //3-hour
        editor.putLong(PrefsConstants.PREFS_SCHEDULE_LAST_FETCH_MILLIS, tournaLastFetch);
        editor.putLong(PrefsConstants.PREFS_SCHEDULE_FETCH_REFRESH_INTERVAL, (24*3600000)); //24-hr (1 days)
        editor.putInt(PrefsConstants.PREFS_SCHEDULE_SPINNER_LAST_POSITION, 0);
        editor.putLong(PrefsConstants.PREFS_CALENDAR_DEFAULT_ID, calId);
        editor.putString(PrefsConstants.PREFS_SCHEDULE_EVENTS_REMINDER, SettingsFragment.DEFAULT_REMINDER);
        editor.putInt(PrefsConstants.PREFS_NEWS_COUNT, 0);

        editor.commit();
    }

    private static void initCalendar() {
        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance.
        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.VISIBLE,                       // 3
                CalendarContract.Calendars.IS_PRIMARY                     // 4
        };

        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_CALENDAR_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_CALENDAR_VISIBLE_INDEX = 3;
        final int PROJECTION_CALENDAR_IS_PRIMARY_INDEX = 4;

        // Run query
        Cursor cursor = null;
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";
        String[] selectionArgs = new String[] {"com.google"};

        // Submit the query and get a Cursor object back.
        cursor = cr.query(uri, EVENT_PROJECTION, null, null, null);

        // Use the cursor to step through the returned records
        long googleCalId = 1;
        while (cursor.moveToNext()) {
            long calID = 0;
            String accountName = null;
            String isVisible = null;
            String displayName = null;
            String isPrimary = null;

            // Get the field values
            calID = cursor.getLong(PROJECTION_ID_INDEX);
            accountName = cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            isVisible = cursor.getString(PROJECTION_CALENDAR_VISIBLE_INDEX);
            displayName = cursor.getString(PROJECTION_CALENDAR_DISPLAY_NAME_INDEX);
            isPrimary = cursor.getString(PROJECTION_CALENDAR_IS_PRIMARY_INDEX);

            // Handle only visible calendars
            if(isVisible.contains("1") && isPrimary.contains("1") && accountName.contains("@gmail")) {
                googleCalId = calID;
                break;
            }
        }

        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(PrefsConstants.PREFS_CALENDAR_DEFAULT_ID, googleCalId);
        editor.commit();
    }

    public Context getAppContext() {
        return mContext;
    }

    public String getAppPackageName() { return mPackageName; }

    public SharedPreferences getAppDefaultPrefs() { return mPrefs; }

    public <T> T getPrefValue(String targetKey, T defaultValue) {
        T prefValue = null;
        Map<String,?> prefsMap = mPrefs.getAll();

        for(Map.Entry<String,?> entry : prefsMap.entrySet())
        {
            if(entry.getKey().equalsIgnoreCase(targetKey)) {
                prefValue = (T)entry.getValue();
                return prefValue;
            }
        }
        return defaultValue;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}