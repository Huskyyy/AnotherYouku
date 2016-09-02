package com.huskyyy.anotheryouku.api;

import android.content.Context;
import android.os.StatFs;
import android.text.TextUtils;

import com.huskyyy.anotheryouku.App;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.util.NetUtils;
import com.huskyyy.anotheryouku.util.Others;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wang on 2016/7/3.
 */
public class ApiHelper {

    public static final String BASE_URL = "https://openapi.youku.com/v2/";
    public static final String OAUTH2_URL = "https://openapi.youku.com/v2/oauth2/authorize";
    public static final String REDIRECT_URL = "http://huskyyy.github.io/";
    //6c4`6_7a5574337g  64g25g4h7b7`5`654`73e2ga5eg_6_5`
    //44beaef554d_c``c  `3ecgae4243c4`e4hfhgfcgc57b54gg5
    public static final String CLIENT_ID = Others.rot47("44beaef554d_c``c");
    public static final String CLIENT_SECRET = Others.rot47("`3ecgae4243c4`e4hfhgfcgc57b54gg5");


    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .addInterceptor(new CacheControlInterceptor())
            .addNetworkInterceptor(new CacheControlInterceptor())
            .cache(getCache())
            .build();
    private static YoukuApi youkuApi = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJavaCallAdapterFactory)
            .build()
            .create(YoukuApi.class);

    public static YoukuApi getYoukuApi() {
//        if(youkuApi == null) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .client(okHttpClient)
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(gsonConverterFactory)
//                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
//                    .build();
//            youkuApi = retrofit.create(YoukuApi.class);
//        }
        return youkuApi;
    }

    private static Cache getCache() {
        File file = new File(App.getGlobalContext().getCacheDir(), "GlobalCache");
        return new Cache(file, calculateDiskCacheSize(file));
    }

    /**
     * This is copied from Picasso
     */
    private static long calculateDiskCacheSize(File dir) {
        long size = 5 * 1024 * 1024;

        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            // Target 2% of the total space.
            size = available / 50;
        } catch (IllegalArgumentException ignored) {
        }

        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, 50 * 1024 * 1024), 5 * 1024 * 1024);
    }

    public static class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {

            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();

            // 无网络时加载缓存
            if(!NetUtils.isNetworkAvailable()) {

                if(!TextUtils.isEmpty(cacheControl)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();

                    Response originalResponse = chain.proceed(request);

                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                            .removeHeader("Pragma")
                            .build();
                } else {
                    return chain.proceed(request);
                }


            }else {
                Response originalResponse = chain.proceed(request);

                if(cacheControl != null && cacheControl.length() != 0) {
                    // 获取请求头的缓存设置，直接设置到响应头中
                    // 这样就可以通过设置请求头来缓存内容
                    return originalResponse.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 对于图片，不作处理
                    return originalResponse;
                }
            }
        }
    }

    public static class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
//            LogUtils.i(String.format("Sending request %s on %s%n%s",
//                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
//            LogUtils.i(String.format("Received response for %s in %.1fms%n%s",
//                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }



}
