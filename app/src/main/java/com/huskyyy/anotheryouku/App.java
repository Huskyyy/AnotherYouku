package com.huskyyy.anotheryouku;

import android.app.Application;
import android.content.Context;

import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.NetUtils;
import com.huskyyy.anotheryouku.util.ToastUtils;
import com.youku.player.base.YoukuPlayerInit;

/**
 * Created by Wang on 2016/7/23.
 */
public class App extends Application {

    private static Context globalContext;

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
        ImageLoader.init(this);
        YoukuPlayerInit.init(this);
    }

    public static Context getGlobalContext() {
        return globalContext;
    }
}
