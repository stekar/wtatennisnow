package com.stekar.apps.sports.wtatennisnow;

import android.app.Activity;
import android.os.Bundle;

import com.stekar.apps.sports.wtatennisnow.fragments.SettingsFragment;

/**
 * Created by stekar_work on 12/16/14.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
