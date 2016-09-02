package com.huskyyy.anotheryouku.activity.main.home;

import com.huskyyy.anotheryouku.activity.base.BasePresenter;
import com.huskyyy.anotheryouku.activity.base.BaseView;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;

import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/7/30.
 */
public interface HomeContract {

    interface Presenter extends BasePresenter {

        void loadAll();

        void openHotRank();

        void refreshCategory(int position, String category);

        void openCategory(String category);

        void openVideo(VideoData data);

    }

    interface View extends BaseView<Presenter> {

        void showAll(List<VideosByCategory> list);

        void showHotRank();

        void showRefreshCategory(int position, VideosByCategory videos, boolean refreshed);

        void showCategory(String category);

        void showWatchVideo(VideoData data);

        void showNoNetworkData();

        void showNoData();

        void showError(String error);

        void addViewSubscription(Subscription subscription);
    }

}
