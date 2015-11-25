package com.stekar.apps.sports.wtatennisnow.fragments;

/**
 * Created by stekar on 11/23/14.
 */

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.loaders.ATPNewsCursorLoader;
import com.stekar.apps.sports.wtatennisnow.sync.SyncUtils;
import com.stekar.apps.sports.wtatennisnow.utils.WindowManagement;
import com.stekar.apps.sports.wtatennisnow.views.adapters.CardsListAdapter;
/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentNews extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnClickListener {

    // Denotes if the GridView has been loaded
    private boolean mIsLoaded;
    private static final String STATE_IS_HIDDEN =
            "com.stekar.apps.sports.wtatennisnow.STATE_IS_HIDDEN";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mHeaderRecent;
    private TextView mHeaderAll;
    private TextView mHeaderStarred;
    private TextView mHeaderShared;
    private TextView mHeaderHidden;
    private int mHeaderSelection;
    public static final int HEADER_SELECTION_RECENT = 0;
    public static final int HEADER_SELECTION_ALL = 1;
    public static final int HEADER_SELECTION_STARRED = 2;
    public static final int HEADER_SELECTION_SHARED = 3;
    public static final int HEADER_SELECTION_HIDDEN = 5;

    public PlaceholderFragmentNews() {
        this.mIsLoaded = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        // IMPORTANT: Setting this causes a non-refresh on landscape mode; empty list view
        //setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        //Toolbar will now take on default actionbar characteristics
        getActivity().setActionBar(toolbar);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mHeaderRecent = (TextView)rootView.findViewById(R.id.headerNewsRecent);
        mHeaderRecent.setOnClickListener(this);

        mHeaderAll = (TextView)rootView.findViewById(R.id.headerNewsAll);
        mHeaderAll.setOnClickListener(this);

        mHeaderStarred = (TextView)rootView.findViewById(R.id.headerNewsStar);
        mHeaderStarred.setOnClickListener(this);

        //mHeaderShared = (TextView)rootView.findViewById(R.id.headerNewsS);
        //mHeaderShared.setOnClickListener(this);

        mHeaderHidden = (TextView)rootView.findViewById(R.id.headerNewsHide);
        mHeaderHidden.setOnClickListener(this);

        mHeaderSelection = HEADER_SELECTION_RECENT;

        WindowManagement.changeStatusBarColor(getActivity(), getResources().getColor(R.color.news_status_bar_color));

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        super.onViewCreated(view, savedInstanceState);
    }

    public void fetchNewsData() {
        ((View)mRecyclerView.getParent()).setBackgroundColor(getResources().getColor(R.color.news_view_fetch_background));
        mRecyclerView.setVisibility(View.GONE);

        SyncUtils.TriggerRefresh(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
        fetchNewsData();
    }

    @Override
    public void onPause() {
        getLoaderManager().destroyLoader(0);
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ATPNewsCursorLoader(getActivity(), mHeaderSelection);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.news_today_card_background));
        mRecyclerView.setVisibility(View.VISIBLE);

        if(mAdapter == null) {
            // specify an adapter (see also next example)
            mAdapter = new CardsListAdapter(data, getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            ((CardsListAdapter)mAdapter).swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CardsListAdapter)mAdapter).swapCursor(null);
    }

    @Override
    public void onClick(View v) {
        if(mAdapter != null) {
            if(v.getId() == R.id.headerNewsRecent) {
                mHeaderRecent.setBackgroundColor(getResources().getColor(R.color.news_tab_background));
                mHeaderRecent.setTextColor(getResources().getColor(R.color.news_tab_foreground));
                mHeaderRecent.setTypeface(null, Typeface.BOLD);

                mHeaderAll.setBackgroundColor(Color.WHITE);
                mHeaderAll.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderAll.setTypeface(null, Typeface.NORMAL);

                mHeaderStarred.setBackgroundColor(Color.WHITE);
                mHeaderStarred.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderStarred.setTypeface(null, Typeface.NORMAL);

                /*mHeaderShared.setBackgroundColor(Color.WHITE);
                mHeaderShared.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderShared.setTypeface(null, Typeface.NORMAL);*/

                mHeaderHidden.setBackgroundColor(Color.WHITE);
                mHeaderHidden.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderHidden.setTypeface(null, Typeface.NORMAL);

                mHeaderSelection = HEADER_SELECTION_RECENT;
            } else if(v.getId() == R.id.headerNewsAll) {
                mHeaderAll.setBackgroundColor(getResources().getColor(R.color.news_tab_background));
                mHeaderAll.setTextColor(getResources().getColor(R.color.news_tab_foreground));
                mHeaderAll.setTypeface(null, Typeface.BOLD);

                mHeaderRecent.setBackgroundColor(Color.WHITE);
                mHeaderRecent.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderRecent.setTypeface(null, Typeface.NORMAL);

                mHeaderStarred.setBackgroundColor(Color.WHITE);
                mHeaderStarred.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderStarred.setTypeface(null, Typeface.NORMAL);

                /*mHeaderShared.setBackgroundColor(Color.WHITE);
                mHeaderShared.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderShared.setTypeface(null, Typeface.NORMAL);*/

                mHeaderHidden.setBackgroundColor(Color.WHITE);
                mHeaderHidden.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderHidden.setTypeface(null, Typeface.NORMAL);

                mHeaderSelection = HEADER_SELECTION_ALL;
            } else if(v.getId() == R.id.headerNewsStar) {
                mHeaderStarred.setBackgroundColor(getResources().getColor(R.color.news_tab_background));
                mHeaderStarred.setTextColor(getResources().getColor(R.color.news_tab_foreground));
                mHeaderStarred.setTypeface(null, Typeface.BOLD);

                mHeaderRecent.setBackgroundColor(Color.WHITE);
                mHeaderRecent.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderRecent.setTypeface(null, Typeface.NORMAL);

                mHeaderAll.setBackgroundColor(Color.WHITE);
                mHeaderAll.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderAll.setTypeface(null, Typeface.NORMAL);

                /*mHeaderShared.setBackgroundColor(Color.WHITE);
                mHeaderShared.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderShared.setTypeface(null, Typeface.NORMAL);*/

                mHeaderHidden.setBackgroundColor(Color.WHITE);
                mHeaderHidden.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderHidden.setTypeface(null, Typeface.NORMAL);

                mHeaderSelection = HEADER_SELECTION_STARRED;
            } /*else if(v.getId() == R.id.headerNewsShare) {
                mHeaderShared.setBackgroundColor(getResources().getColor(R.color.news_tab_background));
                mHeaderShared.setTextColor(getResources().getColor(R.color.news_tab_foreground));
                mHeaderShared.setTypeface(null, Typeface.BOLD);

                mHeaderRecent.setBackgroundColor(Color.WHITE);
                mHeaderRecent.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderRecent.setTypeface(null, Typeface.NORMAL);

                mHeaderAll.setBackgroundColor(Color.WHITE);
                mHeaderAll.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderAll.setTypeface(null, Typeface.NORMAL);

                mHeaderStarred.setBackgroundColor(Color.WHITE);
                mHeaderStarred.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderStarred.setTypeface(null, Typeface.NORMAL);

                mHeaderHidden.setBackgroundColor(Color.WHITE);
                mHeaderHidden.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderHidden.setTypeface(null, Typeface.NORMAL);

                mHeaderSelection = HEADER_SELECTION_SHARED;
            }*/ else {
                mHeaderHidden.setBackgroundColor(getResources().getColor(R.color.news_tab_background));
                mHeaderHidden.setTextColor(getResources().getColor(R.color.news_tab_foreground));
                mHeaderHidden.setTypeface(null, Typeface.BOLD);

                mHeaderRecent.setBackgroundColor(Color.WHITE);
                mHeaderRecent.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderRecent.setTypeface(null, Typeface.NORMAL);

                mHeaderAll.setBackgroundColor(Color.WHITE);
                mHeaderAll.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderAll.setTypeface(null, Typeface.NORMAL);

                mHeaderStarred.setBackgroundColor(Color.WHITE);
                mHeaderStarred.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderStarred.setTypeface(null, Typeface.NORMAL);

                /*mHeaderShared.setBackgroundColor(Color.WHITE);
                mHeaderShared.setTextColor(getResources().getColor(R.color.news_tab_foreground_unselected));
                mHeaderShared.setTypeface(null, Typeface.NORMAL);*/

                mHeaderSelection = HEADER_SELECTION_HIDDEN;
            }
            getLoaderManager().destroyLoader(0);
            getLoaderManager().initLoader(0, null, this);
        }
    }
}
