package com.huskyyy.anotheryouku.activity.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.AuthResult;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.util.NetUtils;

/**
 * Created by Wang on 2016/8/10.
 */
public class MyAccountAuthenticator extends AbstractAccountAuthenticator {

    private Context context;

    public MyAccountAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                             String authTokenType, String[] strings, Bundle options)
            throws NetworkErrorException {

        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                               String authTokenType, Bundle options) throws NetworkErrorException {

        AccountManager am = AccountManager.get(context);
        String authToken = am.peekAuthToken(account, authTokenType);

        // 有AccessToken则直接返回
        if(!TextUtils.isEmpty(authToken)) {
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // 检查是否有refreshToken，有则刷新Token
        // 这会发生在AccessToken失效，AccountManager删除AccessToken时发生（主动删除）
        String refreshToken =  am.getUserData(account, LoginActivity.REFRESH_TOKEN);
        if(!TextUtils.isEmpty(refreshToken)) {
            final AuthResult[] res = new AuthResult[1];
            // 这里不处理断网的情形，即断网情况下直接让用户重新登录
            if(!NetUtils.isNetworkAvailable()) {
                AccountUtils.removeAccount(context);
            } else {
                DataSource.getInstance().getAuthTokenByRefresh(refreshToken,
                        new BaseCallback<AuthResult>() {
                    @Override
                    public void onDataLoaded(AuthResult authResult) {
                        res[0] = authResult;
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        LogUtils.i(error.toString());
                    }
                });
            }


            if(res[0] != null) {
                am.setAuthToken(account, LoginActivity.AUTH_TOKEN_TYPE, res[0].getAccessToken());
                am.setUserData(account, LoginActivity.REFRESH_TOKEN, res[0].getRefreshToken());

                Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                result.putString(AccountManager.KEY_AUTHTOKEN, res[0].getAccessToken());
                return result;
            }
        }

        // 没有任何Token信息，需要用户注册
        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }


    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                     Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                 String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response,
                                           Account account) throws NetworkErrorException {
        return super.getAccountRemovalAllowed(response, account);
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse,
                              Account account, String[] strings) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                    Account account, String s, Bundle bundle)
            throws NetworkErrorException {
        return null;
    }


}

