package com.stekar.apps.sports.wtatennisnow.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

public class ATPScheduleDownloader {
    public static final String ANY = "Any";


    private void setShareText(ATPSchedule scheduleItem) {
        // set-up the share send intent data
        StringBuilder sb = new StringBuilder();
        sb.append("ATP Tournament: ");
        sb.append(scheduleItem.getTournaName());
        sb.append("\n");
        sb.append(scheduleItem.getTournaMonthDisplayName());
        sb.append(", ");
        sb.append(scheduleItem.getTournaDay());
        sb.append("\n");
        sb.append(scheduleItem.getTournaCountry());
        scheduleItem.setTournaShareText(sb.toString());
    }

    private void setWeekNumbers( ArrayList<ATPSchedule> entries) {
        Calendar cal = Calendar.getInstance();

        for(ATPSchedule scheduleItem : entries) {
            cal.clear();
            cal.set(Calendar.MONTH, scheduleItem.getTournaMonth());
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(scheduleItem.getTournaDay()));
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            scheduleItem.setTournaWeekStart(week);

            if(scheduleItem.getTournaSlam() == true) {
                scheduleItem.setTournaWeekEnd(week + 1);
            } else if(scheduleItem.getTournaPremier() == true) {
                if(week == 11 || week == 13) {
                    scheduleItem.setTournaWeekEnd(week + 1);
                } else {
                    scheduleItem.setTournaWeekEnd(week);
                }
            } else {
                scheduleItem.setTournaWeekEnd(week);
            }

            String monthDisplayName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            scheduleItem.setTournaMonthDisplayName(monthDisplayName);
            this.setShareText(scheduleItem);
        }
    }

    private void saveToDatabase(ArrayList<ATPSchedule> tournas,  List<ATPSchedule> mNewTournas) {
        for (ATPSchedule tournaItem : tournas) {
            boolean isRowUpdated = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPTournasHelper().updateTournaItemWinner(tournaItem);
            if(isRowUpdated == false) {
                AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPTournasHelper().insertTournaItem(tournaItem);
            } else {
                mNewTournas.add(tournaItem);
            }
        }
    }

    private String downloadATPSchedule(String urlString) {
        InputStream stream = null;
        String jsonScheduleString = null;

        // Saving JSON data to file
        // if an exception is thrown, do not block the rest
        try {
            stream = downloadUrl(urlString);
            jsonScheduleString = this.fromStream(stream);
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
        return jsonScheduleString;
    }


    public void loadJsonFromNetwork(String urlString, List<ATPSchedule> mNewTournas, boolean manualRefresh) throws XmlPullParserException, IOException {
        ArrayList<ATPSchedule> entries = null;

        try {
            // 1- if a manual sync, always refresh
            // 2- if a scheduled sync, get the data from the disk, before going out to the Network feed
            String jsonScheduleString = null;
            if(manualRefresh || FileUtils.isTimeStampValid(PrefsConstants.PREFS_SCHEDULE_LAST_FETCH_MILLIS, PrefsConstants.PREFS_SCHEDULE_FETCH_REFRESH_INTERVAL) == false) {
                jsonScheduleString = this.downloadATPSchedule(urlString);
                if (jsonScheduleString != null) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Boolean.class, BooleanUtils.booleanAsIntAdapter)
                            .registerTypeAdapter(boolean.class, BooleanUtils.booleanAsIntAdapter)
                            .create();
                    Type collectionType = new TypeToken<ArrayList<ATPSchedule>>() {
                    }.getType();
                    entries = gson.fromJson(jsonScheduleString, collectionType);

                    this.setWeekNumbers(entries);
                    this.saveToDatabase(entries, mNewTournas);
                    FileUtils.saveDownloadTimestampToPrefs(PrefsConstants.PREFS_SCHEDULE_LAST_FETCH_MILLIS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Uploads XML from feeds.feedburner.com/Tennis-AtpWorldTourHeadlineNews, parses it
    // create and return an ATPNews Object
    public void loadJsonFromNetwork(Uri urlString, List<ATPSchedule> mNewTournas, boolean manualRefresh) throws XmlPullParserException, IOException {
        loadJsonFromNetwork(urlString.toString(), mNewTournas, manualRefresh);
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
