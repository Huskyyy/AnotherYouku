package com.huskyyy.anotheryouku.activity.base.fragment;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wang on 2016/7/29.
 */
public class BaseFragment extends Fragment {

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

    public void clearSubscription() {
        getCompositeSubscription().clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getCompositeSubscription().unsubscribe();
    }

}
