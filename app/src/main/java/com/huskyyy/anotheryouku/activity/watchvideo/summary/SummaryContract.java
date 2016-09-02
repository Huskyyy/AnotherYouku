package com.huskyyy.anotheryouku.activity.watchvideo.summary;

import com.huskyyy.anotheryouku.activity.base.BasePresenter;
import com.huskyyy.anotheryouku.activity.base.BaseView;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.data.base.VideoData;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/1.
 */
public interface SummaryContract {

    interface Presenter extends BasePresenter {

        void subscribeUser();

        void shareVideo();

        void markVideo(boolean mark);

    }

    interface View extends BaseView<Presenter> {

        void showVideo(VideoData data);

        void showUser(User user);

        void showMarkFavorite(boolean mark);

        void showSubscribeUser(boolean subscribe);

        void showShare(String title, String link);

        void showNoNetWorkData();

        void showNoVideoData();

        void showNoUserData();

        void showNoSubscribeData();

        void showError(String error);

        void addViewSubscription(Subscription subscription);

    }
}
