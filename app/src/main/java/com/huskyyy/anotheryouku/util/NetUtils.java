package com.huskyyy.anotheryouku.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.huskyyy.anotheryouku.App;
import com.huskyyy.anotheryouku.R;

/**
 * Created by Wang on 2016/7/23.
 */
public class NetUtils {

    private NetUtils() {}

    public static boolean isNetworkAvailable() {

        ConnectivityManager connectivity = (ConnectivityManager) App.getGlobalContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
