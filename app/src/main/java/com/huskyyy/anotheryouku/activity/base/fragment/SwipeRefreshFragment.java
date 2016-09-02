package com.huskyyy.anotheryouku.activity.base.fragment;

import android.os.Bundle;
import android.os.Handler;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.util.LogUtils;

/**
 * Created by Wang on 2016/7/29.
 */
public class SwipeRefreshFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSwipeRefreshLayout(view);
        if(swipeRefreshLayout!= null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if(!isDataInitialized())
                        startRefreshing();
                }
            });
            initData();
        }
    }

    private void setupSwipeRefreshLayout(View root) {
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout);
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });
        }
    }

    protected void initData() {}

    protected void loadData() {}

    protected boolean isDataInitialized() {
        return false;
    }

    protected boolean isRefreshing() {
        return swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing();
    }

    protected void startRefreshing(){
        if(swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    protected void stopRefreshing(){
        if(swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
