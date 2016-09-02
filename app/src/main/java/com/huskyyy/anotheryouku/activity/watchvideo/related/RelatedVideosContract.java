package com.huskyyy.anotheryouku.activity.watchvideo.related;

import com.huskyyy.anotheryouku.activity.base.BasePresenter;
import com.huskyyy.anotheryouku.activity.base.BaseView;
import com.huskyyy.anotheryouku.data.base.VideoData;

import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/7.
 */
public interface RelatedVideosContract {

    interface Presenter extends BasePresenter {

        void openVideosWithKey(String key);

        void openVideo(VideoData videoData);

    }

    interface View extends BaseView<Presenter> {

        void showRelatedVideos(List<String> tags, List<VideoData> videos);

        void showVideosWithKey(String key);

        void showWatchVideo(VideoData data);

        void showNoNetworkData();

        void showNoVideoData(List<String> tags);

        void showError(String error);

        void addViewSubscription(Subscription subscription);

    }
}
