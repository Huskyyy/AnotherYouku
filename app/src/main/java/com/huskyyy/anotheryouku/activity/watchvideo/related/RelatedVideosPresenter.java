package com.huskyyy.anotheryouku.activity.watchvideo.related;

import android.content.Context;
import android.text.TextUtils;

import com.huskyyy.anotheryouku.api.ErrorConstants;
import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByRelated;
import com.huskyyy.anotheryouku.util.NetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/7.
 */
public class RelatedVideosPresenter implements RelatedVideosContract.Presenter {

    private DataSource dataSource;
    private RelatedVideosContract.View relatedVideosView;
    private VideoData videoData;

    public RelatedVideosPresenter(DataSource dataSource,
                                  RelatedVideosContract.View relatedVideosView,
                                  VideoData videoData) {
        this.dataSource = dataSource;
        this.relatedVideosView = relatedVideosView;
        this.videoData = videoData;
        this.relatedVideosView.setPresenter(this);
    }

    @Override
    public void start() {

        final List<String> tags = new ArrayList<>();
        if(!TextUtils.isEmpty(videoData.getTags())) {
            tags.addAll(Arrays.asList(videoData.getTags().split(",")));
        }

        Subscription s = dataSource.getRelatedVideosData(videoData.getId(),
                new BaseCallback<VideosByRelated>() {
                    @Override
                    public void onDataLoaded(VideosByRelated videosByRelated) {
                        List<VideoData> videos =
                                new ArrayList<>(Arrays.asList(videosByRelated.getVideos()));
                        relatedVideosView.showRelatedVideos(tags, videos);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {

                        relatedVideosView.showError(error.toString());
                        relatedVideosView.showNoVideoData(tags);

                    }
                });

        relatedVideosView.addViewSubscription(s);

    }

    @Override
    public void openVideosWithKey(String key) {

    }

    @Override
    public void openVideo(VideoData videoData) {
        if(!NetUtils.isNetworkAvailable())
            relatedVideosView.showNoNetworkData();
        else
            relatedVideosView.showWatchVideo(videoData);
    }
}
