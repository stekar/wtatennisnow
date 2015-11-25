package com.stekar.apps.sports.wtatennisnow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.utils.WindowManagement;

public class ReachabilityFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static ReachabilityFragment newInstance(String param1, String param2) {
        ReachabilityFragment fragment = new ReachabilityFragment();

        return fragment;
    }

    public ReachabilityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WindowManagement.changeStatusBarColor(getActivity(), 0xff636363);

        View rootView = inflater.inflate(R.layout.fragment_reachability, container, false);
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        //Toolbar will now take on default actionbar characteristics
        getActivity().setActionBar(toolbar);

        Toast toast = Toast.makeText(getActivity(), R.string.reachability_not_connected_toast, Toast.LENGTH_LONG);
        toast.show();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
