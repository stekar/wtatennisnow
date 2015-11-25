package com.stekar.apps.sports.wtatennisnow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by stekar on 2/13/15.
 */
public class BitmapUtils {
    private static final String TAG = "TENNISNOW_BTIMAPUTILS";

    public static Bitmap loadFrom(String url, Context context, int defaultDrawable) {
        if(url == null) {
            Log.d(TAG, "URL is null. Returning default Drawable.");
            return BitmapFactory.decodeResource(context.getResources(), defaultDrawable);
        }

        if(url == "") {
            Log.d(TAG, "URL is empty. Returning default Drawable.");
            return BitmapFactory.decodeResource(context.getResources(), defaultDrawable);
        }

        try {
            InputStream is = (InputStream) new URL(url).getContent();
            try {
                return BitmapFactory.decodeStream(is);
            } finally {
                is.close();
            }
        } catch (Exception e) {
            return BitmapFactory.decodeResource(context.getResources(), defaultDrawable);
        }
    }
}
