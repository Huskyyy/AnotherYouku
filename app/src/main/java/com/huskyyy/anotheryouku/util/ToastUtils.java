package com.huskyyy.anotheryouku.util;

import android.content.Context;
import android.widget.Toast;

import com.huskyyy.anotheryouku.App;

/**
 * Created by Wang on 2016/5/11.
 */
public class ToastUtils {

    private ToastUtils() {}

    public static void showShort(int resId) {
        Toast.makeText(App.getGlobalContext(), resId, Toast.LENGTH_SHORT).show();
    }


    public static void showShort(String message) {
        Toast.makeText(App.getGlobalContext(), message, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(int resId) {
        Toast.makeText(App.getGlobalContext(), resId, Toast.LENGTH_LONG).show();
    }


    public static void showLong(String message) {
        Toast.makeText(App.getGlobalContext(), message, Toast.LENGTH_LONG).show();
    }
}
