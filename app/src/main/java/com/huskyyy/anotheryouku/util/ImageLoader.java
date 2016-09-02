package com.huskyyy.anotheryouku.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.huskyyy.anotheryouku.App;
import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.api.ApiHelper;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Wang on 2016/7/29.
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";

    private ImageLoader() {}

    public static void init(Context context){
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(ApiHelper.okHttpClient))
                .build();
        Picasso.setSingletonInstance(picasso);
    }

    // 需要设置placeHolder
    public static void loadNormalImage(String imageUrl, ImageView imageView) {
        if(!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(App.getGlobalContext())
                    .load(imageUrl)
                    .tag(TAG)
                    .into(imageView);
        }
    }

    public static void loadAvatarImage(String imageUrl, ImageView imageView) {
        if(!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(App.getGlobalContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .tag(TAG)
                    .into(imageView);
        }
    }

    public static void pauseImageLoading() {
        Picasso.with(App.getGlobalContext()).pauseTag(TAG);
    }

    public static void resumeImageLoading() {
        Picasso.with(App.getGlobalContext()).resumeTag(TAG);
    }
}
