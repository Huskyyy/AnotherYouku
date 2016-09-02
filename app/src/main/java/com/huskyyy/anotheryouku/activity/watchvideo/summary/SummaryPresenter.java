package com.huskyyy.anotheryouku.activity.watchvideo.summary;

import android.app.Activity;

import com.huskyyy.anotheryouku.activity.account.AccountUtils;
import com.huskyyy.anotheryouku.api.ErrorConstants;
import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.MarkFavoriteResponse;
import com.huskyyy.anotheryouku.data.base.SubscribeUserResponse;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.util.NetUtils;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/2.
 */
public class SummaryPresenter implements SummaryContract.Presenter {

    private DataSource dataSource;
    private SummaryContract.View summaryView;

    private Activity activity;
    private VideoData videoData;
    private User user;

    public SummaryPresenter(Activity activity,
                            DataSource dataSource, SummaryContract.View summaryView,
                            VideoData videoData, User user) {
        this.activity = activity;
        this.dataSource = dataSource;
        this.summaryView = summaryView;
        this.videoData = videoData;
        this.user = user;
        this.summaryView.setPresenter(this);
    }

    @Override
    public void start() {

        summaryView.showVideo(videoData);

        if(!NetUtils.isNetworkAvailable()) {
            summaryView.showNoNetWorkData();
            return;
        }

        Subscription s1 = dataSource.getVideoData(videoData.getId(),
                new BaseCallback<VideoData>() {
                    @Override
                    public void onDataLoaded(VideoData data) {
                        summaryView.showVideo(data);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        if(error.getCode() == ErrorConstants.UNKNOWN_ERROR) {
                            summaryView.showNoVideoData();
                        } else {
                            summaryView.showError(error.toString());
                        }

                    }
                });

        Subscription s2 = dataSource.getUserData(user.getId(), new BaseCallback<User>() {
            @Override
            public void onDataLoaded(User user) {
                summaryView.showUser(user);
            }

            @Override
            public void onDataNotAvailable(Error error) {
                if(error.getCode() == ErrorConstants.UNKNOWN_ERROR) {
                    summaryView.showNoUserData();
                } else {
                    summaryView.showError(error.toString());
                }
            }
        });

        summaryView.addViewSubscription(s1);
        summaryView.addViewSubscription(s2);

        // 没有确认收藏和关注的api，暂时都初始化为没有收藏和关注的状态
        summaryView.showSubscribeUser(false);
        summaryView.showMarkFavorite(false);

    }

    @Override
    public void markVideo(boolean mark) {
        markVideo(mark, 1);
    }

    public void markVideo(final boolean mark, final int count) {

        if(!NetUtils.isNetworkAvailable()) {
            summaryView.showNoNetWorkData();
            return;
        }

        Subscription s = dataSource.markFavoriteVideo(activity, videoData.getId(), mark,
                new BaseCallback<MarkFavoriteResponse>() {
                    @Override
                    public void onDataLoaded(MarkFavoriteResponse markFavoriteResponse) {
                        summaryView.showMarkFavorite(mark);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        // 只利用refreshToken获取一次accessToken
                        if(count == 1 && error.getCode() == ErrorConstants.ACCESS_TOKEN_EXPIRED) {
                            AccountUtils.invalidateAuthToken(activity, null);
                            markVideo(mark, 2);
                        } else {
                            summaryView.showError(error.toString());
                        }
                    }
                });
        summaryView.addViewSubscription(s);
    }

    @Override
    public void subscribeUser() {
        subscribeUser(1);
    }

    public void subscribeUser(final int count) {
        if(!NetUtils.isNetworkAvailable()) {
            summaryView.showNoNetWorkData();
            return;
        }

        Subscription s = dataSource.subscribeUser(activity, user.getId(), user.getName(),
                new BaseCallback<SubscribeUserResponse>() {
                    @Override
                    public void onDataLoaded(SubscribeUserResponse subscribeUserResponse) {
                        summaryView.showSubscribeUser(true);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        // 只利用refreshToken获取一次accessToken
                        if(count == 1
                                && error.getCode() == ErrorConstants.ACCESS_TOKEN_EXPIRED) {
                            AccountUtils.invalidateAuthToken(activity, null);
                            subscribeUser(2);
                        } else if(error.getCode() == ErrorConstants.USER_SUBSCRIBED){
                            summaryView.showSubscribeUser(true);
                        } else {
                            summaryView.showError(error.toString());
                        }
                    }
                });
        summaryView.addViewSubscription(s);
    }

    @Override
    public void shareVideo() {
        summaryView.showShare(videoData.getTitle(), videoData.getLink());
    }
}
