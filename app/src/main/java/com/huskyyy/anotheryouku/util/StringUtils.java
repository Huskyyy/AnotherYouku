package com.huskyyy.anotheryouku.util;

import android.content.Context;
import android.text.TextUtils;

import com.huskyyy.anotheryouku.R;

/**
 * Created by Wang on 2016/7/26.
 */
public class StringUtils {

    private StringUtils() {}

    public static String numberFormatter(int num) {
        if(num > 10000) {
            return "" + num / 10000 + "." + (num % 10000) / 1000 + "万";
        }else {
            return String.valueOf(num);
        }
    }

    public static String numberFormatter(String s) {
        if(TextUtils.isEmpty(s)) {
            return numberFormatter(0);
        }
        int num = Integer.parseInt(s);
        return numberFormatter(num);
    }

    public static String descriptionFormatter(Context context, String s) {
        if(TextUtils.isEmpty(s)) {
            //return context.getString(R.string.message_no_description);
            return "";
        }
        return s;
    }

    public static String publishFormatter(Context context, String s) {
        return context.getString(R.string.publish_prefix) + s;
    }

    public static String imageUrlFormatter(String s) {
        if(TextUtils.isEmpty(s)) {
            return "";
        }
        return s;
    }

    public static String orderFormatter(Context context, String s) {
        return context.getString(R.string.orderby_prefix) + s;
    }

}
