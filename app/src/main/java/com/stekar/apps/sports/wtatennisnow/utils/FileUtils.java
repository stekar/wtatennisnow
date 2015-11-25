package com.stekar.apps.sports.wtatennisnow.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.app.AppController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 * Created by stekar on 12/18/14.
 */
public class FileUtils {
    private static final String TAG = "TENNISNOW_FILE_UTILS";

    public static void saveToDisk(final String data, String filename) {
        File file = new File(AppController.getInstance().getAppContext().getFilesDir(), filename);
        deleteFileIfExist(file);

        FileOutputStream outputStream = null;
        try {
            outputStream = AppController.getInstance().getAppContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // do nohing
            }
        }
    }

    public static boolean isFileStampValid(String filename, long refreshInterval) {
        boolean isFileStampValid = false;

        File file = new File(AppController.getInstance().getAppContext().getFilesDir(), filename);
        if(file.exists() == false) {
            Log.v(TAG, filename + ": File does not exist, aborting reading operation...");
            isFileStampValid = false;
        } else if (file.canRead() == false) {
            Log.v(TAG, filename + ": File cannot be read, aborting reading operation...");
            isFileStampValid = false;
        }

        Calendar rightNow = Calendar.getInstance();
        long currentMilliSeconds = rightNow.getTimeInMillis();
        Log.v(TAG, filename + ": currentMilliSeconds " + String.valueOf(currentMilliSeconds));

        long lastModified = file.lastModified();
        Log.v(TAG, filename + ": lastModified " + String.valueOf(lastModified));

        Log.v(TAG, filename + ": refreshInterval " + String.valueOf(refreshInterval));

        long deltaSync = currentMilliSeconds - lastModified;
        Log.v(TAG, filename + ": deltaSync " + String.valueOf(deltaSync));

        if(deltaSync > refreshInterval) {
            Log.v(TAG, filename + ": File stamp is invalid.");
            isFileStampValid = false;
        } else {
            Log.v(TAG, filename + ": File stamp is valid.");
            isFileStampValid = true;
        }

        return isFileStampValid;
    }

    public static String readFromDisk(String filename) {
        String fileContent = null;

        File file = new File(AppController.getInstance().getAppContext().getFilesDir(), filename);
        if(file.exists() == false || file.canRead() == false) {
            Log.v(TAG, filename + ": File does not exist, aborting reading operation...");
            return fileContent;
        }

        FileInputStream inputStream = null;
        try {
            // create FileInputStream object
            inputStream = new FileInputStream(file);

            byte fileData[] = new byte[(int)file.length()];
            inputStream.read(fileData);
            fileContent = new String(fileData);
            //System.out.println("File content: " + fileContent);
        } catch (FileNotFoundException e) {
           // System.out.println("File not found" + e);
        } catch (IOException ioe) {
           // System.out.println("Exception while reading file " + ioe);
        } finally {
            // close the streams using close method
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException ioe) {
                //System.out.println("Error while closing stream: " + ioe);
            }
        }

        return fileContent;
    }

    private static void deleteFileIfExist(final File fileHandle) {
        if(fileHandle == null) {
            throw new IllegalArgumentException("File handle <fileHandle> is null");
        }

        if(fileHandle.exists()) {
            fileHandle.delete();
        }
    }

    public static void saveDownloadTimestampToPrefs(String prefsKey) {
        Calendar cal = GregorianCalendar.getInstance(new SimpleTimeZone(0, "GMT"));
        long millis = cal.getTimeInMillis();
        Log.d(TAG, prefsKey + " saveDownloadTimestampToPrefs: currentMilliSeconds " + String.valueOf(millis));

        SharedPreferences prefs = AppController.getInstance().getAppDefaultPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(prefsKey, millis);
        editor.commit();
    }

    public static boolean isTimeStampValid(String prefsKey, String prefsKeyRefreshInterval) {
        boolean isFileStampValid = false;

        long lastFetchTime = AppController.getInstance().getPrefValue(prefsKey, 0L);
        Log.d(TAG, prefsKey + " isTimeStampValid: lastFetchTime " + String.valueOf(lastFetchTime));

        long refreshInterval = AppController.getInstance().getPrefValue(prefsKeyRefreshInterval, 0L);
        Log.d(TAG, prefsKey + " isTimeStampValid: refreshInterval " + String.valueOf(refreshInterval));

        Calendar cal = GregorianCalendar.getInstance(new SimpleTimeZone(0, "GMT"));
        long currentMilliSeconds = cal.getTimeInMillis();
        Log.d(TAG, prefsKey + " isTimeStampValid: currentMilliSeconds " + String.valueOf(currentMilliSeconds));

        long deltaSync = currentMilliSeconds - lastFetchTime;
        Log.d(TAG, prefsKey + " isTimeStampValid: deltaSync " + String.valueOf(deltaSync));

        if(deltaSync > refreshInterval) {
            Log.d(TAG, prefsKey + " isTimeStampValid: Prefs stamp is invalid.");
            isFileStampValid = false;
        } else {
            Log.d(TAG, prefsKey + " isTimeStampValid: Prefs stamp is valid.");
            isFileStampValid = true;
        }

        return isFileStampValid;
    }
}