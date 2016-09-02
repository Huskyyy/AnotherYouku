package com.huskyyy.anotheryouku.data;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.huskyyy.anotheryouku.activity.account.LoginActivity;
import com.huskyyy.anotheryouku.api.ErrorConstants;
import com.huskyyy.anotheryouku.data.base.AuthResult;
import com.huskyyy.anotheryouku.data.base.Captcha;
import com.huskyyy.anotheryouku.data.base.Comment;
import com.huskyyy.anotheryouku.data.base.CommentResponse;
import com.huskyyy.anotheryouku.data.base.CommentsByVideo;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.ErrorResponse;
import com.huskyyy.anotheryouku.data.base.MarkFavoriteResponse;
import com.huskyyy.anotheryouku.data.base.SubscribeUserResponse;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.api.ApiHelper;
import com.huskyyy.anotheryouku.data.base.VideosByRelated;
import com.huskyyy.anotheryouku.util.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Wang on 2016/7/23.
 */
public class DataSource {

    private static final DataSource INSTANCE = new DataSource();

    private DataSource() {}

    public static DataSource getInstance() {
        return INSTANCE;
    }

    private Error getError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;

            try {
                ErrorResponse error = new Gson().fromJson(
                        httpException.response().errorBody().string(), ErrorResponse.class);
                if(error != null)
                    return error.getError();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        Error error = new Error();
        error.setCode(ErrorConstants.UNKNOWN_ERROR);
        error.setDescription(e.getMessage());
        error.setE(e);
        return error;

    }

    public Subscription getAllCategoryData(List<String> categories,
                                           final BaseCallback<List<VideosByCategory>> callback) {

        final List<VideosByCategory> tempList = new ArrayList<>();

        Subscription subscription = Observable
                .merge(createCategoryObservables(categories))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideosByCategory>() {
                    @Override
                    public void onCompleted() {
                        callback.onDataLoaded(new ArrayList<VideosByCategory>(tempList));
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                        //e.printStackTrace();
                    }

                    @Override
                    public void onNext(VideosByCategory videosByCate) {
                        tempList.add(videosByCate);
                    }
                });
        return subscription;
    }

    private List<Observable<VideosByCategory>> createCategoryObservables(List<String> categories) {

        List<Observable<VideosByCategory>> res = new ArrayList<>();

        for(String s : categories) {
            res.add(ApiHelper.getYoukuApi()
                    .getVideosByCategory(ApiHelper.CLIENT_ID, s, null, "today", "comment-count", 1, 20));
        }
        return res;
    }

    // 默认按评论数排序
    public Subscription getCategoryData(String category,
                                         final BaseCallback<VideosByCategory> callback) {

        return getCategoryData(category, null, "comment-count", 1, 20, callback);
    }

    // 获取该类别下的所有视频
    public Subscription getCategoryData(String category, String orderBy, int page, int count,
                                        final BaseCallback<VideosByCategory> callback) {
        return getCategoryData(category, null, orderBy, page, count, callback);
    }

    // 细分子类
    public Subscription getCategoryData(String category, String genre,
                                        String orderBy, int page, int count,
                                        final BaseCallback<VideosByCategory> callback) {
        Subscription subscription = ApiHelper.getYoukuApi()
                .getVideosByCategory(ApiHelper.CLIENT_ID, category, genre, "today", orderBy, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideosByCategory>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(VideosByCategory videosByCate) {
                        callback.onDataLoaded(videosByCate);
                    }
                });
        return subscription;
    }

    public Subscription getVideosByTag(String category, String tag, String orderBy, int page,
                                       int count, final BaseCallback<VideosByCategory> callback) {
        Subscription subscription = ApiHelper.getYoukuApi()
                .getVideosByTag(ApiHelper.CLIENT_ID, category, tag, "today", orderBy, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideosByCategory>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(VideosByCategory videosByCate) {
                        callback.onDataLoaded(videosByCate);
                    }
                });
        return subscription;
    }


    public Subscription getUserData(long userId, final BaseCallback<User> callback) {
        Subscription s = ApiHelper.getYoukuApi()
                .getUserData(ApiHelper.CLIENT_ID, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(User user) {
                        callback.onDataLoaded(user);
                    }
                });
        return s;
    }

    public Subscription getVideoData(String videoId, final BaseCallback<VideoData> callback) {
        Subscription s = ApiHelper.getYoukuApi()
                .getVideoData(ApiHelper.CLIENT_ID, videoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(VideoData data) {
                        callback.onDataLoaded(data);
                    }
                });
        return s;
    }

    public Subscription getVideoAndUserData(String videoId, long userId,
                                            final BaseCallback<VideoData> callback) {
        Subscription s = Observable
                .zip(ApiHelper.getYoukuApi().getVideoData(ApiHelper.CLIENT_ID, videoId),
                        ApiHelper.getYoukuApi().getUserData(ApiHelper.CLIENT_ID, userId),
                        new Func2<VideoData, User, VideoData>() {
                            @Override
                            public VideoData call(VideoData data, User user) {
                                data.setUser(user);
                                return data;
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(VideoData data) {
                        callback.onDataLoaded(data);
                    }
                });
        return s;
    }

    public Subscription getCommentsData(String videoId, int page, int count,
                                        final BaseCallback<List<Comment>> callback) {

        Subscription s = ApiHelper.getYoukuApi()
                .getCommentsByVideo(ApiHelper.CLIENT_ID, videoId, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentsByVideo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(CommentsByVideo commentsByVideo) {
                        List<Comment> list = new ArrayList<Comment>(
                                Arrays.asList(commentsByVideo.getComments()));
                        callback.onDataLoaded(list);
                    }
                });
        return s;

    }

    public Subscription getUsersData(List<Comment> comments,
                                     final BaseCallback<List<User>> callback) {

        final List<User> users = new ArrayList<>();
        Subscription s = Observable
                .merge(createUserObservableList(comments))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                        callback.onDataLoaded(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(User user) {
                        users.add(user);
                    }
                });
        return s;
    }

    private List<Observable<User>> createUserObservableList(List<Comment> comments) {

        List<Observable<User>> res = new ArrayList<>();

        for(Comment comment : comments) {
            res.add(ApiHelper.getYoukuApi()
                    .getUserData(ApiHelper.CLIENT_ID, comment.getUser().getId()));
        }
        return res;
    }

    public Subscription getHotCommentsData(final String videoId, int page, int count,
                                           final BaseCallback<List<Comment>> callback) {

        Subscription s = ApiHelper.getYoukuApi()
                .getHotCommentsByVideo(ApiHelper.CLIENT_ID, videoId, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentsByVideo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //LogUtils.i(getError(e).toString());
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(CommentsByVideo commentsByVideo) {
                        //LogUtils.i(videoId + " " + Arrays.toString(commentsByVideo.getComments()));
                        callback.onDataLoaded(
                                new ArrayList<Comment>(Arrays.asList(commentsByVideo.getComments())));
                    }
                });
        return s;

    }

    public Subscription getRelatedVideosData(String videoId,
                                             final BaseCallback<VideosByRelated> callback) {

        Subscription s = ApiHelper.getYoukuApi()
                .getVideosByRelated(ApiHelper.CLIENT_ID, videoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideosByRelated>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(VideosByRelated videosByRelated) {
                        callback.onDataLoaded(videosByRelated);
                    }
                });

        return s;
    }

    public Subscription getAuthTokenByCode(String code, final BaseCallback<AuthResult> callback) {
        Subscription s = ApiHelper.getYoukuApi()
                .getAuthTokenByCode(ApiHelper.CLIENT_ID, ApiHelper.CLIENT_SECRET,
                        "authorization_code", code, ApiHelper.REDIRECT_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AuthResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(AuthResult authResult) {
                        callback.onDataLoaded(authResult);
                    }
                });
        return s;
    }

    public Subscription getAuthTokenByRefresh(String refreshToken,
                                              final BaseCallback<AuthResult> callback) {
        Subscription s = ApiHelper.getYoukuApi()
                .getAuthTokenByRefresh(ApiHelper.CLIENT_ID, ApiHelper.CLIENT_SECRET,
                        "refresh_token", refreshToken)
                .subscribe(new Observer<AuthResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(AuthResult authResult) {
                        callback.onDataLoaded(authResult);
                    }
                });
        return s;
    }

    public Subscription getAccountData(String accessToken, final BaseCallback<User> callback) {

        Subscription s = ApiHelper.getYoukuApi()
                .getAccountData(ApiHelper.CLIENT_ID, accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(User user) {
                        callback.onDataLoaded(user);
                    }
                });
        return s;
    }

    public Subscription getAccountData(final Activity activity, final BaseCallback<User> callback) {

        AccountManager accountManager = AccountManager.get(activity);
        Account account = new Account(LoginActivity.ACCOUNT_NAME, LoginActivity.ACCOUNT_TYPE);
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                LoginActivity.AUTH_TOKEN_TYPE, null, activity, null, null);

        Subscription s = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String token = null;
                        try {
                            token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                        if(TextUtils.isEmpty(token)) {
                            subscriber.onError(new Throwable("No AuthToken!"));
                        }
                        subscriber.onNext(token);
                    }
                })
                .flatMap(new Func1<String, Observable<User>>() {
                    @Override
                    public Observable<User> call(String accessToken) {
                        return ApiHelper.getYoukuApi()
                                .getAccountData(ApiHelper.CLIENT_ID, accessToken);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(User user) {
                        callback.onDataLoaded(user);
                    }
                });
        return s;
    }

    public Subscription markFavoriteVideo(String accessToken, String videoId, boolean mark,
                                          final BaseCallback<MarkFavoriteResponse> callback) {

        Observable<MarkFavoriteResponse> observable = null;
        if(mark) {
            observable = ApiHelper.getYoukuApi()
                    .markFavoriteVideo(ApiHelper.CLIENT_ID, accessToken, videoId);
        } else {
            observable = ApiHelper.getYoukuApi()
                    .unMarkFavoriteVideo(ApiHelper.CLIENT_ID, accessToken, videoId);
        }

        Subscription s = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MarkFavoriteResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(MarkFavoriteResponse markFavoriteResponse) {
                        callback.onDataLoaded(markFavoriteResponse);
                    }
                });
        return s;
    }

    public Subscription markFavoriteVideo(Activity activity,
                                          final String videoId, final boolean mark,
                                          final BaseCallback<MarkFavoriteResponse> callback) {

        AccountManager accountManager = AccountManager.get(activity);
        Account account = new Account(LoginActivity.ACCOUNT_NAME, LoginActivity.ACCOUNT_TYPE);
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                LoginActivity.AUTH_TOKEN_TYPE, null, activity, null, null);

        Subscription s = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String token = null;
                        try {
                            token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                        if(TextUtils.isEmpty(token)) {
                            subscriber.onError(new Throwable("No AuthToken!"));
                        }
                        subscriber.onNext(token);
                    }
                })
                .flatMap(new Func1<String, Observable<MarkFavoriteResponse>>() {
                    @Override
                    public Observable<MarkFavoriteResponse> call(String accessToken) {
                        if(mark) {
                            return ApiHelper.getYoukuApi()
                                    .markFavoriteVideo(ApiHelper.CLIENT_ID, accessToken, videoId);
                        } else {
                            return ApiHelper.getYoukuApi()
                                    .unMarkFavoriteVideo(ApiHelper.CLIENT_ID, accessToken, videoId);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MarkFavoriteResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(MarkFavoriteResponse markFavoriteResponse) {
                        callback.onDataLoaded(markFavoriteResponse);
                    }
                });
        return s;

    }

    public Subscription subscribeUser(String accessToken, long userId, String userName,
                                      final BaseCallback<SubscribeUserResponse> callback) {

        Subscription s = ApiHelper.getYoukuApi()
                .subscribeUser(ApiHelper.CLIENT_ID, accessToken, userId, userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SubscribeUserResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(SubscribeUserResponse subscribeUserResponse) {
                        callback.onDataLoaded(subscribeUserResponse);
                    }
                });
        return s;
    }

    public Subscription subscribeUser(Activity activity,
                                      final long userId, final String userName,
                                      final BaseCallback<SubscribeUserResponse> callback) {

        AccountManager accountManager = AccountManager.get(activity);
        Account account = new Account(LoginActivity.ACCOUNT_NAME, LoginActivity.ACCOUNT_TYPE);
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                LoginActivity.AUTH_TOKEN_TYPE, null, activity, null, null);

        Subscription s = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String token = null;
                        try {
                            token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                        if(TextUtils.isEmpty(token)) {
                            subscriber.onError(new Throwable("No AuthToken!"));
                        }
                        subscriber.onNext(token);
                    }
                })
                .flatMap(new Func1<String, Observable<SubscribeUserResponse>>() {
                    @Override
                    public Observable<SubscribeUserResponse> call(String accessToken) {
                        return ApiHelper.getYoukuApi()
                                .subscribeUser(ApiHelper.CLIENT_ID, accessToken, userId, userName);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SubscribeUserResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(SubscribeUserResponse subscribeUserResponse) {
                        callback.onDataLoaded(subscribeUserResponse);
                    }
                });
        return s;
    }

    public Subscription commentVideo(String accessToken, String videoId, String content,
                                     String replyId, String captchaKey, String captchaText,
                                     final BaseCallback<CommentResponse> callback) {
        Subscription s = ApiHelper.getYoukuApi()
                .commentVideo(ApiHelper.CLIENT_ID, accessToken, videoId, content,
                        replyId, captchaKey, captchaText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(CommentResponse response) {
                        callback.onDataLoaded(response);
                    }
                });
        return s;
    }

    public Subscription commentVideo(Activity activity, final String videoId, final String content,
                                     final String replyId,
                                     final String captchaKey, final String captchaText,
                                     final BaseCallback<CommentResponse> callback) {

        //LogUtils.i(content);

        AccountManager accountManager = AccountManager.get(activity);
        Account account = new Account(LoginActivity.ACCOUNT_NAME, LoginActivity.ACCOUNT_TYPE);
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                LoginActivity.AUTH_TOKEN_TYPE, null, activity, null, null);

        Subscription s = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String token = null;
                        try {
                            token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                        if(TextUtils.isEmpty(token)) {
                            subscriber.onError(new Throwable("No AuthToken!"));
                        }
                        subscriber.onNext(token);
                    }
                })
                .flatMap(new Func1<String, Observable<CommentResponse>>() {
                    @Override
                    public Observable<CommentResponse> call(String accessToken) {
                        return ApiHelper.getYoukuApi()
                                .commentVideo(ApiHelper.CLIENT_ID, accessToken, videoId, content,
                                        replyId, captchaKey, captchaText);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(CommentResponse response) {
                        //LogUtils.i("comment" + response.toString());
                        callback.onDataLoaded(response);
                    }
                });
        return s;
    }

    public Subscription cancelCommentVideo(String accessToken, String commentId,
                                           final BaseCallback<CommentResponse> callback) {
        Subscription s = ApiHelper.getYoukuApi()
                .cancelCommentVideo(ApiHelper.CLIENT_ID, accessToken, commentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(CommentResponse commentResponse) {
                        callback.onDataLoaded(commentResponse);
                    }
                });
        return s;
    }

    public Subscription cancelCommentVideo(Activity activity, final String commentId,
                                           final BaseCallback<CommentResponse> callback) {

        AccountManager accountManager = AccountManager.get(activity);
        Account account = new Account(LoginActivity.ACCOUNT_NAME, LoginActivity.ACCOUNT_TYPE);
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                LoginActivity.AUTH_TOKEN_TYPE, null, activity, null, null);

        Subscription s = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String token = null;
                        try {
                            token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                        if(TextUtils.isEmpty(token)) {
                            subscriber.onError(new Throwable("No AuthToken!"));
                        }
                        subscriber.onNext(token);
                    }
                })
                .flatMap(new Func1<String, Observable<CommentResponse>>() {
                    @Override
                    public Observable<CommentResponse> call(String accessToken) {
                        return ApiHelper.getYoukuApi()
                                .cancelCommentVideo(ApiHelper.CLIENT_ID, accessToken, commentId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(CommentResponse commentResponse) {
                        //LogUtils.i("remove" + commentResponse.toString());
                        callback.onDataLoaded(commentResponse);
                    }
                });
        return s;
    }

    public Subscription getCaptchaData(final BaseCallback<Captcha> callback) {

        Subscription s = ApiHelper.getYoukuApi()
                .getCaptchaData(ApiHelper.CLIENT_ID)
                .map(new Func1<Captcha, Captcha>() {
                    @Override
                    public Captcha call(Captcha captcha) {
                        // base64转图片
                        byte[] bytes = Base64.decode(captcha.getData(), Base64.DEFAULT);
                        captcha.setBitmap(
                                BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        return captcha;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Captcha>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(Captcha captcha) {
                        callback.onDataLoaded(captcha);
                    }
                });
        return s;
    }

    public Subscription getCommentData(final String commentId, final BaseCallback<Comment> callback) {

        Subscription s = ApiHelper.getYoukuApi()
                .getCommentData(ApiHelper.CLIENT_ID, commentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable(getError(e));
                    }

                    @Override
                    public void onNext(Comment comment) {
                        callback.onDataLoaded(comment);
                    }
                });
        return s;
    }


}
