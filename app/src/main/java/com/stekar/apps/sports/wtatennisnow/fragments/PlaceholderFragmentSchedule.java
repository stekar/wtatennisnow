package com.stekar.apps.sports.wtatennisnow.fragments;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.constants.PrefsConstants;
import com.stekar.apps.sports.wtatennisnow.loaders.ATPTournasCursorLoader;
import com.stekar.apps.sports.wtatennisnow.sync.SyncUtils;
import com.stekar.apps.sports.wtatennisnow.utils.WindowManagement;
import com.stekar.apps.sports.wtatennisnow.views.adapters.ATPScheduleCardsListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceholderFragmentSchedule extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnClickListener, AdapterView.OnItemSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner mSpinnerMonth;
    private TextView mHeaderGrandSlams;
    private TextView mHeaderMasters1000;
    private TextView mHeaderAll;
    private TextView mHeaderStarred;
    private HeaderSelection mHeaderSelection;
    private int mSpinnerLastPosition;
    private static final String SPINNER_LAST_POSITION = "SPINNER_LAST_POSITION";
    private static final int DEFAULT_SPINNER_POSITION = 0;

    public PlaceholderFragmentSchedule() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        // IMPORTANT: Setting this causes a non-refresh on landscape mode; empty list view
        //setRetainInstance(true);
        WindowManagement.changeStatusBarColor(getActivity(), getResources().getColor(R.color.tourna_status_bar_color));

        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        //Toolbar will now take on default actionbar characteristics
        getActivity().setActionBar(toolbar);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSchedule);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //setup the month
        // try landscape first
        mSpinnerMonth = (Spinner) rootView.findViewById(R.id.spinnerMonthLandscape);
        if(mSpinnerMonth == null) {
            mSpinnerMonth = (Spinner) rootView.findViewById(R.id.spinnerMonth);
        }
        mSpinnerMonth.setOnItemSelectedListener(this);

        mHeaderGrandSlams = (TextView)rootView.findViewById(R.id.headerGrandSlams);
        mHeaderGrandSlams.setOnClickListener(this);

        mHeaderMasters1000 = (TextView)rootView.findViewById(R.id.headerMasters1000);
        mHeaderMasters1000.setOnClickListener(this);

        mHeaderAll = (TextView)rootView.findViewById(R.id.headerAll);
        mHeaderAll.setOnClickListener(this);

        mHeaderStarred = (TextView)rootView.findViewById(R.id.headerStar);
        mHeaderStarred.setOnClickListener(this);

        mHeaderSelection = new HeaderSelection();
        mHeaderSelection.setIsAll();

        if (savedInstanceState == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance().getAppContext());
            mSpinnerLastPosition = prefs.getInt(PrefsConstants.PREFS_SCHEDULE_SPINNER_LAST_POSITION, DEFAULT_SPINNER_POSITION);
            mSpinnerMonth.setSelection(mSpinnerLastPosition);
        } else {
            mSpinnerLastPosition = savedInstanceState.getInt(SPINNER_LAST_POSITION);
            mSpinnerMonth.setSelection(mSpinnerLastPosition);
        }


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mSpinnerMonth != null) {
            outState.putInt(SPINNER_LAST_POSITION, mSpinnerMonth.getSelectedItemPosition());
        } else {
            outState.putInt(SPINNER_LAST_POSITION, DEFAULT_SPINNER_POSITION);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
        fetchScheduleData();
    }

    @Override
    public void onPause() {
        getLoaderManager().destroyLoader(0);
        super.onPause();
    }

    public void fetchScheduleData() {
        ((View)mRecyclerView.getParent()).setBackgroundColor(getResources().getColor(R.color.schedule_view_fetch_background));
        mRecyclerView.setVisibility(View.GONE);

        SyncUtils.TriggerRefresh(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ATPTournasCursorLoader(getActivity(), mHeaderSelection);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.tourna_today_card_background));
        mRecyclerView.setVisibility(View.VISIBLE);

        if(mAdapter == null) {
            mAdapter = new ATPScheduleCardsListAdapter(data, getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            ((ATPScheduleCardsListAdapter)mAdapter).swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((ATPScheduleCardsListAdapter)mAdapter).swapCursor(null);
    }

    @Override
    public void onClick(View v) {
        if(mAdapter != null) {
            if (v.getId() == R.id.headerGrandSlams) {
                mHeaderSelection.setIsSlam();

                mHeaderGrandSlams.setBackgroundColor(getResources().getColor(R.color.tourna_tab_background));
                mHeaderGrandSlams.setTextColor(getResources().getColor(R.color.tourna_tab_foreground));
                mHeaderGrandSlams.setTypeface(null, Typeface.BOLD);

                mHeaderMasters1000.setBackgroundColor(Color.WHITE);
                mHeaderMasters1000.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderMasters1000.setTypeface(null, Typeface.NORMAL);

                mHeaderAll.setBackgroundColor(Color.WHITE);
                mHeaderAll.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderAll.setTypeface(null, Typeface.NORMAL);

                mHeaderStarred.setBackgroundColor(Color.WHITE);
                mHeaderStarred.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderStarred.setTypeface(null, Typeface.NORMAL);
            } else if (v.getId() == R.id.headerMasters1000) {
                mHeaderSelection.setIsMasters();

                mHeaderMasters1000.setBackgroundColor(getResources().getColor(R.color.tourna_tab_background));
                mHeaderMasters1000.setTextColor(getResources().getColor(R.color.tourna_tab_foreground));
                mHeaderMasters1000.setTypeface(null, Typeface.BOLD);

                mHeaderGrandSlams.setBackgroundColor(Color.WHITE);
                mHeaderGrandSlams.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderGrandSlams.setTypeface(null, Typeface.NORMAL);

                mHeaderAll.setBackgroundColor(Color.WHITE);
                mHeaderAll.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderAll.setTypeface(null, Typeface.NORMAL);

                mHeaderStarred.setBackgroundColor(Color.WHITE);
                mHeaderStarred.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderStarred.setTypeface(null, Typeface.NORMAL);
            } else if (v.getId() == R.id.headerAll) {
                mHeaderSelection.setIsAll();

                mHeaderAll.setBackgroundColor(getResources().getColor(R.color.tourna_tab_background));
                mHeaderAll.setTextColor(getResources().getColor(R.color.tourna_tab_foreground));
                mHeaderAll.setTypeface(null, Typeface.BOLD);

                mHeaderGrandSlams.setBackgroundColor(Color.WHITE);
                mHeaderGrandSlams.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderGrandSlams.setTypeface(null, Typeface.NORMAL);

                mHeaderMasters1000.setBackgroundColor(Color.WHITE);
                mHeaderMasters1000.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderMasters1000.setTypeface(null, Typeface.NORMAL);

                mHeaderStarred.setBackgroundColor(Color.WHITE);
                mHeaderStarred.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderStarred.setTypeface(null, Typeface.NORMAL);
            } else {
                mHeaderSelection.setIsStarred();

                mHeaderStarred.setBackgroundColor(getResources().getColor(R.color.tourna_tab_background));
                mHeaderStarred.setTextColor(getResources().getColor(R.color.tourna_tab_foreground));
                mHeaderStarred.setTypeface(null, Typeface.BOLD);

                mHeaderAll.setBackgroundColor(Color.WHITE);
                mHeaderAll.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderAll.setTypeface(null, Typeface.NORMAL);

                mHeaderGrandSlams.setBackgroundColor(Color.WHITE);
                mHeaderGrandSlams.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderGrandSlams.setTypeface(null, Typeface.NORMAL);

                mHeaderMasters1000.setBackgroundColor(Color.WHITE);
                mHeaderMasters1000.setTextColor(getResources().getColor(R.color.tourna_tab_foreground_unselected));
                mHeaderMasters1000.setTypeface(null, Typeface.NORMAL);
            }
            getLoaderManager().destroyLoader(0);
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(mAdapter != null) {
            SharedPreferences prefs = AppController.getInstance().getAppDefaultPrefs();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(PrefsConstants.PREFS_SCHEDULE_SPINNER_LAST_POSITION, position);
            editor.commit();

            mSpinnerLastPosition = position;
            mHeaderSelection.monthPos = mSpinnerLastPosition;

            getLoaderManager().destroyLoader(0);
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class HeaderSelection {
        private boolean mIsMasters;
        private boolean mIsSlam;
        private boolean mIsAll;
        private boolean mIsStarred;
        public  int monthPos;

        public HeaderSelection() {
            monthPos = mSpinnerLastPosition;
        }

        public boolean getIsSlam() { return mIsSlam; }
        public boolean getIsMasters() { return mIsMasters; }
        public boolean getIsAll() { return mIsAll; }

        public void setIsAll() {
            mIsAll = true;
            mIsSlam = mIsMasters = mIsStarred = false;
        }

        public void setIsMasters() {
            mIsMasters = true;
            mIsSlam = mIsAll = mIsStarred = false;
        }

        public void setIsSlam() {
            mIsSlam = true;
            mIsMasters = mIsAll = mIsStarred = false;
        }

        public void setIsStarred() {
            mIsStarred = true;
            mIsMasters = mIsAll = mIsSlam = false;
        }
    }
}
