package com.huskyyy.anotheryouku.activity.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Wang on 2016/8/13.
 */
public class AuthenticatorService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        MyAccountAuthenticator authenticator = new MyAccountAuthenticator(this);
        return authenticator.getIBinder();
    }
}
