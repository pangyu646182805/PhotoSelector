package com.ppyy.photoselector.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/11/10.
 */

public class LogUtils {
    private static final String TAG = "PhotoSelector";
    private static boolean sGlobalToggle = true;

    private LogUtils() {
    }

    public static void i(String msg) {
        if (sGlobalToggle) Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (sGlobalToggle) Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (sGlobalToggle) Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (sGlobalToggle) Log.v(TAG, msg);
    }

    public static void w(String msg) {
        if (sGlobalToggle) Log.w(TAG, msg);
    }
}
