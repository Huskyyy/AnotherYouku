package com.huskyyy.anotheryouku.data.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Wang on 2016/8/15.
 */
public class Captcha {

    private String key;
    // 验证码图片数据，base64编码
    private String data;

    private Bitmap bitmap;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "Captcha{" +
                "key='" + key + '\'' +
                '}';
    }
}
