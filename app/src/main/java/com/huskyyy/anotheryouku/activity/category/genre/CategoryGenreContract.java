package com.huskyyy.anotheryouku.activity.category.genre;

import com.huskyyy.anotheryouku.activity.base.BasePresenter;
import com.huskyyy.anotheryouku.activity.base.BaseView;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;

import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/29.
 */
public interface CategoryGenreContract {

    interface Presenter extends BasePresenter {

        void loadData(String tag, String orderBy);

        void openVideo(VideoData data);

        void loadMore(String tag, String orderBy, int page);

    }

    interface View extends BaseView<Presenter> {

        void showTags(List<String> tags);

        void showData(VideosByCategory videos);

        void showWatchVideo(VideoData data);

        void showLoadMore(VideosByCategory videos);

        void showNoNetworkData();

        void showNoData();

        void showLoadMoreFailed();

        void showError(String error);

        void addViewSubscription(Subscription subscription);

        void clearViewSubscription();
    }
}
