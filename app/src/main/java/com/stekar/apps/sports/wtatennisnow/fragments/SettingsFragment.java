package com.stekar.apps.sports.wtatennisnow.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPNewsClearDeletedNews;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPNewsClearHiddenNews;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPNewsDeletedNewsPrefsCount;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPNewsHiddenNewsPrefsCount;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPScheduleEventsPrefsCount;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPSettingsClearEvents;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.constants.PrefsConstants;

import java.util.concurrent.ExecutorService;

/**
 * Created by stekar_work on 12/16/14.
        */
public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "TENNISNOW_SettingsFragment";
    private ExecutorService mExecService;
    static int sEventsRemoved;
    public static final String DEFAULT_REMINDER = "60";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // ATP News
        final Preference newsDeletedPref = findPreference(PrefsConstants.PREFS_NEWS_DELETED_CLEAR);
        // Display the number of deleted news
        new AsyncATPNewsDeletedNewsPrefsCount().execute(newsDeletedPref);

        newsDeletedPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AsyncATPNewsClearDeletedNews().execute();

                // Reset to default state
                newsDeletedPref.setSummary(getResources().getString(R.string.prefs_app_news_deleted_clear_summary_empty));
                return true;
            }
        });

        final Preference newsHiddenPref = findPreference(PrefsConstants.PREFS_NEWS_HIDDEN_CLEAR);
        // Display the number of hidden news
        new AsyncATPNewsHiddenNewsPrefsCount().execute(newsHiddenPref);

        newsHiddenPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AsyncATPNewsClearHiddenNews().execute();

                // Reset to default state
                newsHiddenPref.setSummary(getResources().getString(R.string.prefs_app_news_hidden_clear_summary_empty));
                return true;
            }
        });

        // ATP Tournaments
        final Preference eventClearPref = findPreference(PrefsConstants.PREFS_SCHEDULE_EVENTS_CLEAR);
        // Display the number of events
        new AsyncATPScheduleEventsPrefsCount().execute(eventClearPref);

        eventClearPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AsyncATPSettingsClearEvents().execute();

                // Reset to default state
                eventClearPref.setSummary(getResources().getString(R.string.prefs_app_tourna_events_clear_summary_empty));
                return true;
            }
        });

        final ListPreference remindersListPref = (ListPreference)findPreference(PrefsConstants.PREFS_SCHEDULE_EVENTS_REMINDER);
        remindersListPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String reminder = (String)newValue;
                SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                editor.putString(PrefsConstants.PREFS_SCHEDULE_EVENTS_REMINDER, reminder);
                editor.apply();

                return true;
            }
        });

        // Notifications
        final Preference notifNewsEnablePref = findPreference(PrefsConstants.PREFS_NOTIF_NEWS_ENABLE);
        notifNewsEnablePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean){
                    Boolean boolVal = (Boolean)newValue;

                    SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                    editor.putBoolean(PrefsConstants.PREFS_NOTIF_NEWS_ENABLE, boolVal);
                    editor.apply();
                }
                return true;
            }
        });

        final Preference notifTournasEnablePref = findPreference(PrefsConstants.PREFS_NOTIF_TOURNAS_ENABLE);
        notifTournasEnablePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean){
                    Boolean boolVal = (Boolean)newValue;

                    SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                    editor.putBoolean(PrefsConstants.PREFS_NOTIF_TOURNAS_ENABLE, boolVal);
                    editor.apply();
                }
                return true;
            }
        });

        // About
        final Preference aboutIconographyPref = findPreference(PrefsConstants.PREFS_ABOUT_ICONOGRAPHY);
        aboutIconographyPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Constants.ICONS4ANDROID_SITE_URL));
                getActivity().startActivity(i);
                return true;
            }
        });
    }
}
