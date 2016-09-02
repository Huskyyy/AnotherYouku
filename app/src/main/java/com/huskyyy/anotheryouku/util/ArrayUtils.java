package com.huskyyy.anotheryouku.util;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Wang on 2016/7/30.
 */
public class ArrayUtils {

    private ArrayUtils() {}

    public static String[] getStringArray(Context context, int arrayId) {
        return context.getResources().getStringArray(arrayId);
    }

    public static ArrayList<String> getStringList(Context context, int arrayId) {
        return new ArrayList<>(Arrays.asList(getStringArray(context, arrayId)));
    }

    public static int[] getIdArray(Context context, int arrayId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(arrayId);
        int length = typedArray.length();
        int[] ids = new int[length];
        for(int i = 0; i < length; i++) {
            ids[i] = typedArray.getResourceId(i, -1);
        }
        typedArray.recycle();
        return ids;
    }

    public static ArrayList<Integer> getIdList(Context context, int arrayId) {
        int[] tmp = getIdArray(context, arrayId);
        ArrayList<Integer> res = new ArrayList<>();
        for(int i : tmp) {
            res.add(i);
        }
        return res;
    }
}
