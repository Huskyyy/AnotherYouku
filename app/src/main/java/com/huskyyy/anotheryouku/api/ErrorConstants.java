package com.huskyyy.anotheryouku.api;

/**
 * Created by Wang on 2016/8/11.
 */
public class ErrorConstants {

    private ErrorConstants() {}

    public static final int UNKNOWN_ERROR = -1;
    public static final int RATE_LIMITED= 1017;
    public static final int SERVICE_EXCEPTION = 1002;
    public static final int ACCESS_TOKEN_EXPIRED = 1009;
    public static final int USER_SUBSCRIBED = 130020302;
    // 需要验证码
    public static final int NEED_CAPTCHA = 130030056;
    // 验证码错误
    public static final int CAPTCHA_INVALID = 1018;
}
