package com.huskyyy.anotheryouku.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Created by Wang on 2016/7/31.
 */
public class PreferencesHelper {

    private SharedPreferences sharedPreferences;
    private Context context;

    public PreferencesHelper(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public PreferencesHelper(Context context, String s) {
        this.context =context;
        sharedPreferences = context.getSharedPreferences(s, Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public void saveLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

}
