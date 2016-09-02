package com.huskyyy.anotheryouku.activity.category.home;

import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.util.NetUtils;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/23.
 */
public class CategoryHomePresenter implements CategoryHomeContract.Presenter {

    private DataSource dataSource;
    private CategoryHomeContract.View homeView;
    private String category;

    public CategoryHomePresenter(DataSource dataSource,
                                 CategoryHomeContract.View homeView,
                                 String category) {
        this.dataSource = dataSource;
        this.homeView = homeView;
        this.category = category;
        this.homeView.setPresenter(this);
    }

    @Override
    public void start() {
        if(!NetUtils.isNetworkAvailable()) {
            homeView.showNoNetworkData();
            homeView.showNoData();
            return;
        }
        Subscription s = dataSource.getCategoryData(category, "published", 1, CategoryHomeAdapter.PAGE_COUNT,
                new BaseCallback<VideosByCategory>() {
                    @Override
                    public void onDataLoaded(VideosByCategory videosByCate) {
                        homeView.showAll(videosByCate);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        homeView.showError(error.toString());
                        homeView.showNoData();
                    }
                });
        homeView.addViewSubscription(s);
    }

    @Override
    public void loadMore(int page) {
        if(!NetUtils.isNetworkAvailable()) {
            homeView.showNoNetworkData();
            homeView.showLoadMoreFailed();
            return;
        }
        Subscription s = dataSource.getCategoryData(category, "published", page, CategoryHomeAdapter.PAGE_COUNT,
                new BaseCallback<VideosByCategory>() {
                    @Override
                    public void onDataLoaded(VideosByCategory videosByCate) {
                        homeView.showLoadMore(videosByCate);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        homeView.showError(error.toString());
                        homeView.showLoadMoreFailed();
                    }
                });
        homeView.addViewSubscription(s);
    }

    @Override
    public void openHotRank() {
        if(!NetUtils.isNetworkAvailable()) {
            homeView.showNoNetworkData();
            return;
        }
        homeView.showHotRank();
    }

    @Override
    public void openVideo(VideoData data) {
        if(!NetUtils.isNetworkAvailable()) {
            homeView.showNoNetworkData();
            return;
        }
        homeView.showWatchVideo(data);
    }
}
