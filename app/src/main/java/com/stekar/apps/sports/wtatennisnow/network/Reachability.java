package com.stekar.apps.sports.wtatennisnow.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by stekar on 12/21/14.
 */
public class Reachability {
    private final static String TAG = "TENNISNOW_REACHABILITY";
    private final static int WIFI_ENABLED_FLAG = 0;
    private final static int CELLULAR_ENABLED_FLAG = 1;

    public static boolean isNetworkAvailable(Context ctx) {
        boolean isConnected = false;

        ConnectivityManager connMgr =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            Log.v(TAG, "Network is available.");
            isConnected = true;
        }

        return isConnected;
    }

    public static boolean isNetworkAvailable(Context ctx, boolean[] connectivityFlags)
    {
        if(connectivityFlags == null || connectivityFlags.length < 2) {
            Log.v(TAG, "Connectivity flags not initialized properly.");
            return false;
        }

        ConnectivityManager connMgr =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            Log.v(TAG, "Network is available.");

            boolean wifiEnabled = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean cellularEnabled = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            connectivityFlags[WIFI_ENABLED_FLAG] = wifiEnabled;
            connectivityFlags[CELLULAR_ENABLED_FLAG] = cellularEnabled;

            return true;
        } else {
            Log.v(TAG, "Network is not available.");
            return false;
        }
    }

    public static boolean isWiFiAvailable(Context ctx)
    {
        boolean flags[] = new boolean[2];
        boolean isConnected = isNetworkAvailable(ctx, flags);
        if(isConnected && flags[WIFI_ENABLED_FLAG]) {
            Log.v(TAG, "Network is available, and WiFi is ON.");
            return true;
        } else {
            Log.v(TAG, "Network is either not available, or WiFi is OFF.");
            return false;
        }
    }
}
