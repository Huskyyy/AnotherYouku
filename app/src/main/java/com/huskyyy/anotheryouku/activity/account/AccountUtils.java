package com.huskyyy.anotheryouku.activity.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.huskyyy.anotheryouku.util.PreferencesHelper;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wang on 2016/8/15.
 */
public class AccountUtils {

    private AccountUtils() {}

    public static Subscription getAccountAuthToken(Activity activity,
                                                   final GetAuthTokenCallback callback) {

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
                        subscriber.onNext(token);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        if(TextUtils.isEmpty(s)) {
                            callback.onError(new Throwable("No AuthToken"));
                        } else {
                            callback.onAuthTokenLoaded(s);
                        }
                    }
                });
        return s;
    }

    public interface GetAuthTokenCallback {

        void onAuthTokenLoaded(String authToken);

        void onError(Throwable e);
    }

    public static void invalidateAuthToken(Context context, String authToken) {
        AccountManager.get(context).invalidateAuthToken(LoginActivity.ACCOUNT_TYPE, authToken);
    }

    public static Account[] getAccounts(Context context, String type) {
        return AccountManager.get(context).getAccountsByType(type);
    }

    // 异步
    public static void removeAccount(Context context) {
        Account account = new Account(LoginActivity.ACCOUNT_NAME, LoginActivity.ACCOUNT_TYPE);
        AccountManager.get(context).removeAccount(account, null, null);
        // 清除保存的id
        setAccountId(context, -1);
    }

    public static void setAccountId(Context context, long id) {
        PreferencesHelper helper = new PreferencesHelper(context, "Account");
        helper.saveLong("AccountId", id);
    }

    public static long getAccountId(Context context) {
        PreferencesHelper helper = new PreferencesHelper(context, "Account");
        return helper.getLong("AccountId");
    }
}
