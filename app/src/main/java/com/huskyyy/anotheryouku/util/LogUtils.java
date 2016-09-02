package com.huskyyy.anotheryouku.util;

import android.util.Log;

public class LogUtils {

    private static final String TAG = "AnotherYouku";
    private static final boolean DEBUG = true;

    private LogUtils() {}

    public static void d(String message) {
        if(DEBUG)
            Log.d(TAG, buildMessage(message));
    }

    public static void e(String message) {
        if(DEBUG)
            Log.e(TAG, buildMessage(message));
    }

    public static void i(String message) {
        if(DEBUG)
            Log.i(TAG, buildMessage(message));
    }

    public static void v(String message) {
        if(DEBUG)
            Log.v(TAG, buildMessage(message));
    }

    public static void w(String message) {
        if(DEBUG)
            Log.w(TAG, buildMessage(message));
    }

    private static String buildMessage(String rawMessage) {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String fullClassName = caller.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return className + "." + caller.getMethodName() + "(): " + rawMessage;
    }
}
