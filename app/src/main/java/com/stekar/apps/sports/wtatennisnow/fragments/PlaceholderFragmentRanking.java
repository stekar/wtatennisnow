package com.stekar.apps.sports.wtatennisnow.fragments;

/**
 * Created by stekar on 11/23/14.
 */

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.loaders.ATPPlayerRankingCursorLoader;
import com.stekar.apps.sports.wtatennisnow.sync.SyncUtils;
import com.stekar.apps.sports.wtatennisnow.utils.WindowManagement;
import com.stekar.apps.sports.wtatennisnow.views.adapters.ATPRankingCardsListAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentRanking extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mHeaderTopPlayers;
    private TextView mHeaderAllPlayers;
    private TextView mHeaderTitlesYtdPlayers;
    private int mHeaderSelection;
    public static final int HEADER_SELECTION_TOP_PLAYERS = 0;
    public static final int HEADER_SELECTION_TITLES_YTD_PLAYERS = 1;
    public static final int HEADER_SELECTION_ALL_PLAYERS = 2;

    public PlaceholderFragmentRanking() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        //setRetainInstance(true);

        WindowManagement.changeStatusBarColor(getActivity(), getResources().getColor(R.color.ranking_status_bar_color));

        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        //Toolbar will now take on default actionbar characteristics
        getActivity().setActionBar(toolbar);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewRanking);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mHeaderTopPlayers = (TextView)rootView.findViewById(R.id.headerRankingTopPlayers);
        mHeaderTopPlayers.setOnClickListener(this);

        mHeaderTitlesYtdPlayers = (TextView)rootView.findViewById(R.id.headerRankingTitlesYtdPlayers);
        mHeaderTitlesYtdPlayers.setOnClickListener(this);

        mHeaderAllPlayers = (TextView)rootView.findViewById(R.id.headerRankingAllPlayers);
        mHeaderAllPlayers.setOnClickListener(this);

        mHeaderSelection = HEADER_SELECTION_TOP_PLAYERS;

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
        fetchRankingData();
    }

    @Override
    public void onPause() {
        getLoaderManager().destroyLoader(0);
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ATPPlayerRankingCursorLoader(getActivity(), mHeaderSelection);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.ranking_today_card_background));
        mRecyclerView.setVisibility(View.VISIBLE);

        if (mAdapter == null) {
            mAdapter = new ATPRankingCardsListAdapter(data, getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            ((ATPRankingCardsListAdapter) mAdapter).swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((ATPRankingCardsListAdapter)mAdapter).swapCursor(null);
    }

    public void fetchRankingData() {
        SyncUtils.TriggerRefresh(false);
    }

    @Override
    public void onClick(View v) {
        if(mAdapter != null) {
            if (v.getId() == R.id.headerRankingTopPlayers) {
                mHeaderTopPlayers.setBackgroundColor(getResources().getColor(R.color.ranking_tab_background));
                mHeaderTopPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground));
                mHeaderTopPlayers.setTypeface(null, Typeface.BOLD);

                mHeaderTitlesYtdPlayers.setBackgroundColor(Color.WHITE);
                mHeaderTitlesYtdPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground_unselected));
                mHeaderTitlesYtdPlayers.setTypeface(null, Typeface.NORMAL);

                mHeaderAllPlayers.setBackgroundColor(Color.WHITE);
                mHeaderAllPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground_unselected));
                mHeaderAllPlayers.setTypeface(null, Typeface.NORMAL);

                mHeaderSelection = HEADER_SELECTION_TOP_PLAYERS;

                getLoaderManager().destroyLoader(0);
                getLoaderManager().initLoader(0, null, this);
            } else if (v.getId() == R.id.headerRankingTitlesYtdPlayers) {
                mHeaderTitlesYtdPlayers.setBackgroundColor(getResources().getColor(R.color.ranking_tab_background));
                mHeaderTitlesYtdPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground));
                mHeaderTitlesYtdPlayers.setTypeface(null, Typeface.BOLD);

                mHeaderTopPlayers.setBackgroundColor(Color.WHITE);
                mHeaderTopPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground_unselected));
                mHeaderTopPlayers.setTypeface(null, Typeface.NORMAL);

                mHeaderAllPlayers.setBackgroundColor(Color.WHITE);
                mHeaderAllPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground_unselected));
                mHeaderAllPlayers.setTypeface(null, Typeface.NORMAL);

                mHeaderSelection = HEADER_SELECTION_TITLES_YTD_PLAYERS;

                getLoaderManager().destroyLoader(0);
                getLoaderManager().initLoader(0, null, this);
            } else {
                /*mHeaderAllPlayers.setBackgroundColor(getResources().getColor(R.color.ranking_tab_background));
                mHeaderAllPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground));
                mHeaderAllPlayers.setTypeface(null, Typeface.BOLD);

                mHeaderTopPlayers.setBackgroundColor(Color.WHITE);
                mHeaderTopPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground_unselected));
                mHeaderTopPlayers.setTypeface(null, Typeface.NORMAL);

                mHeaderTitlesYtdPlayers.setBackgroundColor(Color.WHITE);
                mHeaderTitlesYtdPlayers.setTextColor(getResources().getColor(R.color.ranking_tab_foreground_unselected));
                mHeaderTitlesYtdPlayers.setTypeface(null, Typeface.NORMAL);
                */

                //mHeaderSelection = HEADER_SELECTION_ALL_PLAYERS;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Constants.RANKING_ALL_PLAYERS_SITE_URL));
                getActivity().startActivity(i);
            }
        }
    }
}
