package com.stekar.apps.sports.wtatennisnow.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stekar.apps.sports.wtatennisnow.constants.PrefsConstants;
import com.stekar.apps.sports.wtatennisnow.parsers.*;
import com.stekar.apps.sports.wtatennisnow.models.*;
import com.stekar.apps.sports.wtatennisnow.utils.FileUtils;

public class NewsDownloader {
    public static final String ANY = "Any";

    private String downloadATPPlayers(String urlString) {
        InputStream stream = null;
        String jsonPlayersString = null;

        // Saving JSON data to file
        // if an exception is thrown, do not block the rest
        try {
            stream = downloadUrl(urlString);
            jsonPlayersString = this.fromStream(stream);
            //FileUtils.saveToDisk(jsonScheduleString, Constants.atpScheduleFilename);
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
        return jsonPlayersString;
    }

    private String downloadATPNewsFeed(String urlString) {
        InputStream stream = null;
        String xmlStringToParse = null;

        // Saving JSON data to file
        // if an exception is thrown, do not block the rest
        try {
            stream = downloadUrl(urlString);
            String string = this.fromStream(stream);
            int xmlStartIndex = string.indexOf("<item>");
            String xmlStringAtStartIndex = string.substring(xmlStartIndex);
            xmlStringToParse = "<channel>" + xmlStringAtStartIndex;
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
        return xmlStringToParse;
    }

    // Uploads XML from feeds.feedburner.com/Tennis-AtpWorldTourHeadlineNews, parses it
    // create and return an ATPNews Object
    public boolean loadXmlFromNetwork(String urlString, List<ATPNews> newNews, final boolean manualRefresh) throws XmlPullParserException, IOException {
        boolean areNewsUpdated = false;

        try {
            boolean isTimeStampValid = FileUtils.isTimeStampValid(PrefsConstants.PREFS_NEWS_LAST_FETCH_MILLIS, PrefsConstants.PREFS_NEWS_FETCH_REFRESH_INTERVAL);

            // 1- if a manual sync, always refresh
            // 2- if a scheduled sync, get the data from the disk, before going out to the Network feed
            String xmlStringToParse = null;
            if(manualRefresh == true || isTimeStampValid == false) {
                xmlStringToParse = this.downloadATPNewsFeed(urlString);
                if(xmlStringToParse != null) {
                    // Download the players photos
                    String url = "https://docs.google.com/document/export?format=txt&confirm=no_antivirus&id=1HMw4yuZvdiLKTjGSNOE0VvhngEZmy9_8vKTDzuvs_N4";
                    String jsonPlayersString =this.downloadATPPlayers(url);
                    Gson gson = new Gson();
                    java.lang.reflect.Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
                    Map<String,String> mapPlayers = gson.fromJson(jsonPlayersString, stringStringMap);

                    // Parse the ATP News Feed and insert into the Database
                    ATPNewsParser newsParser = new ATPNewsParser();
                    newsParser.parse(new StringReader(xmlStringToParse), mapPlayers, newNews);
                    FileUtils.saveDownloadTimestampToPrefs(PrefsConstants.PREFS_NEWS_LAST_FETCH_MILLIS);
                }

                /*SharedPreferences.Editor editor = AppController.getInstance().getAppDefaultPrefs().edit();
                editor.putInt(PrefsConstants.PREFS_NEWS_COUNT, newNews.size());
                editor.commit();*/

                if(newNews.size() > 0 && isTimeStampValid == false) {
                    areNewsUpdated = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            areNewsUpdated = false;
        }

        return areNewsUpdated;
    }

    // Uploads XML from feeds.feedburner.com/Tennis-AtpWorldTourHeadlineNews, parses it
    // create and return an ATPNews Object
    public boolean loadXmlFromNetwork(Uri urlString, List<ATPNews> newNews, final boolean manualRefresh) throws XmlPullParserException, IOException {
        return loadXmlFromNetwork(urlString.toString(), newNews, manualRefresh);
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
