package com.huskyyy.anotheryouku.activity.account;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.api.ApiHelper;
import com.huskyyy.anotheryouku.api.ErrorConstants;
import com.huskyyy.anotheryouku.activity.base.activity.BaseActivity;
import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.AuthResult;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class LoginActivity extends BaseActivity {

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    public static final String PREFIX = "com.huskyyy.anotheryouku.activity.account.";
    public static final String ACCOUNT_NAME = "AnotherYouku";
    public static final String ACCOUNT_TYPE = PREFIX + "ACCOUNT_TYPE";
    public static final String AUTH_TOKEN_TYPE = PREFIX + "AUTH_TOKEN_TYPE";
    public static final String REFRESH_TOKEN = PREFIX + "REFRESH_TOKEN";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webview)
    WebView webView;

    private ProgressDialog progressDialog;
    private boolean redirected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupWebView();

        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    private void setupWebView() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.authorization_processing));
        progressDialog.setIndeterminate(true);

        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                redirected = true;
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if(url.startsWith(ApiHelper.REDIRECT_URL)) {
                    webView.stopLoading();
                }

                if(redirected) {
                    LogUtils.i(url);
                    if(url.startsWith(ApiHelper.REDIRECT_URL)) {
                        progressDialog.show();
                        String code = url.split("code=")[1].split("&")[0];
                        LogUtils.i(code);
                        getToken(code);
                    }
                    redirected = false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        String url = "https://openapi.youku.com/v2/oauth2/authorize?client_id="
                + ApiHelper.CLIENT_ID
                + "&response_type=code&redirect_uri=http://huskyyy.github.io/";
        webView.loadUrl(url);
    }

    private void getToken(String code) {
        Subscription s = DataSource.getInstance().getAuthTokenByCode(code, new BaseCallback<AuthResult>() {
            @Override
            public void onDataLoaded(final AuthResult authResult) {
                progressDialog.dismiss();
                LogUtils.i(authResult.toString());

                Subscription s1 = DataSource.getInstance().getAccountData(authResult.getAccessToken(), new BaseCallback<User>() {
                    @Override
                    public void onDataLoaded(User user) {
                        // 每次授权完都记录用户ID，以便之后验证登录用户（否则每次都得请求获取用户信息）
                        AccountUtils.setAccountId(LoginActivity.this, user.getId());
                        finishLogin(authResult);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        progressDialog.dismiss();
                        if(error.getCode() == ErrorConstants.UNKNOWN_ERROR) {
                            ToastUtils.showShort(R.string.authorization_failed);
                        } else {
                            ToastUtils.showShort(error.getDescription());
                        }
                    }
                });
                addSubscription(s1);
            }

            @Override
            public void onDataNotAvailable(Error error) {
                progressDialog.dismiss();
                if(error.getCode() == ErrorConstants.UNKNOWN_ERROR) {
                    ToastUtils.showShort(R.string.authorization_failed);
                } else {
                    ToastUtils.showShort(error.getDescription());
                }
            }
        });
        addSubscription(s);
    }

    private void finishLogin(AuthResult authResult) {

        Account account = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
        AccountManager accountManager = AccountManager.get(this);
        // 默认只有一个用户，若之前有用户注册过，则直接覆盖
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if(accounts == null || accounts.length == 0) {
            accountManager.addAccountExplicitly(account, null, null);
        }
        accountManager.setAuthToken(account, AUTH_TOKEN_TYPE, authResult.getAccessToken());
        accountManager.setUserData(account, REFRESH_TOKEN, authResult.getRefreshToken());

        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, ACCOUNT_NAME);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authResult.getAccessToken());

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(Activity.RESULT_OK, intent);
        finish();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }

        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
        webView.clearCache(true);
        webView.clearHistory();

        super.finish();
    }

}

