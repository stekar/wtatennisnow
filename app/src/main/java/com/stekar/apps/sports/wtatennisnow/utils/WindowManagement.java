package com.stekar.apps.sports.wtatennisnow.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by stekar on 12/14/14.
 */
public class WindowManagement {
    public static void changeStatusBarColor(final Activity targetActivity, int targetColor) {
        Window window = targetActivity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(targetColor);
    }
}
