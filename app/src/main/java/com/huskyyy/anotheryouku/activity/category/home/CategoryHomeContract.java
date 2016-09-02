package com.huskyyy.anotheryouku.activity.category.home;

import com.huskyyy.anotheryouku.activity.base.BasePresenter;
import com.huskyyy.anotheryouku.activity.base.BaseView;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;

import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/23.
 */
public interface CategoryHomeContract {

    interface Presenter extends BasePresenter {

        void openHotRank();

        void openVideo(VideoData data);

        void loadMore(int page);

    }

    interface View extends BaseView<Presenter> {

        void showAll(VideosByCategory videos);

        void showHotRank();

        void showWatchVideo(VideoData data);

        void showLoadMore(VideosByCategory videos);

        void showNoNetworkData();

        void showNoData();

        void showLoadMoreFailed();

        void showError(String error);

        void addViewSubscription(Subscription subscription);
    }

}
