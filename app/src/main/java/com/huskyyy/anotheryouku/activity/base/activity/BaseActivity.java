package com.huskyyy.anotheryouku.activity.base.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wang on 2016/7/29.
 */
public class BaseActivity extends AppCompatActivity {

    private CompositeSubscription compositeSubscription;

    public CompositeSubscription getCompositeSubscription() {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }

        return compositeSubscription;
    }


    public void addSubscription(Subscription s) {
        getCompositeSubscription().add(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }
}
