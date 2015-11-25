package com.stekar.apps.sports.wtatennisnow;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.controllers.FragmentController;
import com.stekar.apps.sports.wtatennisnow.fragments.*;
import com.stekar.apps.sports.wtatennisnow.network.Reachability;
import com.stekar.apps.sports.wtatennisnow.sync.SyncUtils;

import android.content.Intent;

public class MainActivity extends Activity {
    private final String TAG = "TENNISNOW_MAIN_ACTIVITY";
    private FragmentController mFragmentController;
    private Fragment mCurFragment;
    private boolean mIsFragmentSwapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mCurFragment = getFragmentManager().findFragmentById(R.id.container);

        // Are we coming from a Notification maybe?
        Intent intent = getIntent();
        if(intent != null && intent.getAction().equalsIgnoreCase(Constants.ACTION_NOTIFICATION)) {
            int fragmentType = intent.getIntExtra(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_NEWS_TYPE);
            if (fragmentType == Constants.FRAGMENT_SCHEDULE_TYPE) {
                this.mCurFragment = new PlaceholderFragmentSchedule();
            } else if (fragmentType == Constants.FRAGMENT_NEWS_TYPE) {
                this.mCurFragment = new PlaceholderFragmentNews();
            } else {
                this.mCurFragment = null;
            }
        }

        if (savedInstanceState == null || this.mCurFragment == null) {
            if(this.mCurFragment == null) {
                this.mCurFragment = new PlaceholderFragmentNews();
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.container, this.mCurFragment)
                    .commit();
        }

        if(this.mCurFragment instanceof PlaceholderFragmentNews) {
            this.initFragmentController(this.mCurFragment, Constants.FRAGMENT_NEWS_TYPE);
        } else if(this.mCurFragment instanceof PlaceholderFragmentRanking) {
            this.initFragmentController(this.mCurFragment, Constants.FRAGMENT_RANKING_TYPE);
        } else if(this.mCurFragment instanceof PlaceholderFragmentSchedule) {
            this.initFragmentController(this.mCurFragment, Constants.FRAGMENT_SCHEDULE_TYPE);
        } else {
            this.initFragmentController(this.mCurFragment, Constants.FRAGMENT_REACHABILITY_TYPE);
        }
    }

    /*
    * This callback is invoked when the system is about to destroy the Activity.
    */
    @Override
    public void onDestroy() {
        //unregisterBroadcastReceivers();
        this.unregisterBroadcastReceivers();
        // Must always call the super method at the end.
        super.onDestroy();
    }

    @Override
    public void onResume() {
        // if the user was not connected previously (i.e. before the activity was paused)
        // then connectivity got restored, then we put the user back
        // to the default fragment, instead of staying ont he reachability fragment
        if(Reachability.isNetworkAvailable(this) == true) {
            if(mFragmentController.getFragmentType() == Constants.FRAGMENT_REACHABILITY_TYPE) {
                this.swapFragmentPlaceholder(new PlaceholderFragmentNews(), Constants.FRAGMENT_NEWS_TYPE);
            }
        } else {
            this.swapFragmentPlaceholder(new ReachabilityFragment(), Constants.FRAGMENT_REACHABILITY_TYPE);
        }

        // Setup broadcast receivers
        this.registerBroadcastReceivers();
        super.onResume();
    }

    @Override
    public void onPause() {
        //unregisterBroadcastReceivers();
        this.unregisterBroadcastReceivers();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if(mIsFragmentSwapped == true) {
            switch(mFragmentController.getFragmentType()) {
                case Constants.FRAGMENT_NEWS_TYPE:
                    MenuItem item = menu.getItem(0);
                    item.setIcon(R.drawable.ic_action_news_pressed);

                    MenuItem item1a = menu.getItem(1);
                    item1a.setIcon(R.drawable.ic_action_ranking);

                    MenuItem item1b = menu.getItem(2);
                    item1b.setIcon(R.drawable.ic_action_tournas);

                    break;
                case Constants.FRAGMENT_RANKING_TYPE:
                    MenuItem item2 = menu.getItem(1);
                    item2.setIcon(R.drawable.ic_action_ranking_pressed);

                    MenuItem item2a = menu.getItem(0);
                    item2a.setIcon(R.drawable.ic_action_news);

                    MenuItem item2b = menu.getItem(2);
                    item2b.setIcon(R.drawable.ic_action_tournas);

                    break;
                case Constants.FRAGMENT_SCHEDULE_TYPE:
                    MenuItem item3 = menu.getItem(2);
                    item3.setIcon(R.drawable.ic_action_tournas_pressed);

                    MenuItem item3a = menu.getItem(0);
                    item3a.setIcon(R.drawable.ic_action_news);

                    MenuItem item3b = menu.getItem(1);
                    item3b.setIcon(R.drawable.ic_action_ranking);

                    break;
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            this.unregisterBroadcastReceivers();
            this.registerBroadcastReceivers();

            if(isReachabilityFragmentShown()) {
                return true;
            } else {
                // Force a refresh of all feeds
                SyncUtils.TriggerRefresh(true);
            }

            return true;
        } else if (id == R.id.action_show_news) {
            this.swapFragmentPlaceholder(new PlaceholderFragmentNews(), Constants.FRAGMENT_NEWS_TYPE);
            return true;
        } else if (id == R.id.action_show_ranking) {
             this.swapFragmentPlaceholder(new PlaceholderFragmentRanking(), Constants.FRAGMENT_RANKING_TYPE);
            return true;
        } else if (id == R.id.action_show_schedule) {
            this.swapFragmentPlaceholder(new PlaceholderFragmentSchedule(), Constants.FRAGMENT_SCHEDULE_TYPE);
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initFragmentController(Fragment newFragment, int fragmentType) {
        if(this.mFragmentController == null) {
            this.mFragmentController = new FragmentController(this, newFragment, fragmentType);
        } else {
            this.mFragmentController.setCurFragment(newFragment);
            this.mFragmentController.setFragmentType(fragmentType);
        }

        mIsFragmentSwapped = true;
    }

    private void swapFragmentPlaceholder(Fragment newFragmentPlaceholder, int pos) {
        boolean isConnected = Reachability.isNetworkAvailable(this);

        this.unregisterBroadcastReceivers();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // automatically set the reachability fragment upon any fragment switching
        if(isConnected) {
            this.mCurFragment = newFragmentPlaceholder;
        } else {
            Log.v(TAG, "swapFragmentPlaceholder :: showing the reachability fragment instead of the requested " + String.valueOf(pos));
            this.mCurFragment =  new ReachabilityFragment();
            pos = Constants.FRAGMENT_REACHABILITY_TYPE;
        }
        fragmentTransaction.replace(R.id.container, this.mCurFragment);
        fragmentTransaction.commit();

        this.initFragmentController(this.mCurFragment, pos);

        this.registerBroadcastReceivers();
    }

    private void registerBroadcastReceivers() {
        this.mFragmentController.registerBroadcastReceivers();
    }

    private void unregisterBroadcastReceivers() {
        this.mFragmentController.unregisterBroadcastReceivers();
    }

    private boolean isReachabilityFragmentShown() {
        boolean isConnected = Reachability.isNetworkAvailable(this);
        if(isConnected == false) {
            Log.v(TAG, "isReachabilityFragmentShown :: User is not connected, showing the reachability fragment.");
            this.swapFragmentPlaceholder(new ReachabilityFragment(), Constants.FRAGMENT_REACHABILITY_TYPE);

            return true;
        } else {
            return false;
        }
    }
}