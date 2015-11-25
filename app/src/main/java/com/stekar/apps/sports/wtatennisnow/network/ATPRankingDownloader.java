package com.stekar.apps.sports.wtatennisnow.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.constants.PrefsConstants;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.models.*;
import com.stekar.apps.sports.wtatennisnow.utils.BooleanUtils;
import com.stekar.apps.sports.wtatennisnow.utils.FileUtils;

public class ATPRankingDownloader {
    public static final String ANY = "Any";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;

    // The user's current network preference setting.
    public static String sPref = null;

    private void saveToDatabase(ArrayList<ATPRanking> rankings) {
        for (ATPRanking rankingItem : rankings) {
            this.setShareText(rankingItem);
            boolean isRowUpdated = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPPlayerRankingHelper().updatePlayerRankingItem(rankingItem);
            if(isRowUpdated == false) {
                AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPPlayerRankingHelper().insertPlayerRankingItem(rankingItem);
            }
        }
    }

    private String downloadRanking(String urlString) {
        InputStream stream = null;
        String jsonRankingString = null;

        // Saving JSON data to file
        // if an exception is thrown, do not block the rest
        try {
            stream = downloadUrl(urlString);
            jsonRankingString = this.fromStream(stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
                //do nothing
            }
        }
        return jsonRankingString;
    }

    private void setShareText(ATPRanking rankingItem) {
        // set-up the share send intent data
        StringBuilder sb = new StringBuilder();
        sb.append("ATP Ranking: ");
        sb.append(rankingItem.getPlayerLastName());
        sb.append("[");
        sb.append(rankingItem.getPlayerRank());
        sb.append("]");
        sb.append("\n");
        sb.append(rankingItem.getPlayerTotalPoints());
        rankingItem.setShareText(sb.toString());
    }

    public void loadJsonFromNetwork(String urlString, boolean manualRefresh) throws XmlPullParserException, IOException {
        ArrayList<ATPRanking> entries = null;

        try {
            String jsonRankingString = null;
            if(manualRefresh || FileUtils.isTimeStampValid(PrefsConstants.PREFS_RANKING_LAST_FETCH_MILLIS, PrefsConstants.PREFS_RANKING_FETCH_REFRESH_INTERVAL) == false) {
                jsonRankingString = this.downloadRanking(urlString);
                if (jsonRankingString != null) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Boolean.class, BooleanUtils.booleanAsIntAdapter)
                            .registerTypeAdapter(boolean.class, BooleanUtils.booleanAsIntAdapter)
                            .create();
                    Type collectionType = new TypeToken<ArrayList<ATPRanking>>() {
                    }.getType();
                    entries = gson.fromJson(jsonRankingString, collectionType);

                    this.saveToDatabase(entries);
                    FileUtils.saveDownloadTimestampToPrefs(PrefsConstants.PREFS_RANKING_LAST_FETCH_MILLIS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Uploads XML from feeds.feedburner.com/Tennis-AtpWorldTourHeadlineNews, parses it
    // create and return an ATPNews Object
    public void loadJsonFromNetwork(Uri urlString, boolean manualRefresh) throws XmlPullParserException, IOException {
        loadJsonFromNetwork(urlString.toString(), manualRefresh);
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    private String fromStream(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }
}
