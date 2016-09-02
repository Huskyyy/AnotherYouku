package com.huskyyy.anotheryouku.activity.main.home;

import android.content.Context;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.util.ArrayUtils;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.util.NetUtils;
import com.huskyyy.anotheryouku.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rx.Subscription;

/**
 * Created by Wang on 2016/7/30.
 */
public class HomePresenter implements HomeContract.Presenter {

    private Context context;
    private DataSource dataSource;
    private HomeContract.View homeView;

    private String[] categories;
    private int[] logoIds;
    private Random random = new Random();

    public HomePresenter(Context context, DataSource dataSource, HomeContract.View homeView) {
        this.context = context;
        this.dataSource = dataSource;
        this.homeView = homeView;
        this.homeView.setPresenter(this);

        initData(context);
    }

    private void initData(Context context) {
        if(categories == null)
            categories = ArrayUtils.getStringArray(context, R.array.home_category);
        if(logoIds == null)
            logoIds = ArrayUtils.getIdArray(context, R.array.category_imgs);
    }

    private List<VideosByCategory> shuffleData(List<VideosByCategory> data) {
        List<VideosByCategory> res = new ArrayList<>();
        for(VideosByCategory item : data) {
            // 这里复制一份热门视频数据作为banner的数据，banner一共有5页
            if(res.size() == 0) {
                VideosByCategory bannerData = new VideosByCategory();
                VideoData[] videos = new VideoData[5];
                if(item.getVideos() != null && item.getVideos().length == 20) {
                    for(int i = 0; i < 5; i++) {
                        VideoData target = item.getVideos()[random.nextInt(4) + 4 * i];
                        VideoData tmp = new VideoData();
                        tmp.setId(target.getId());
                        tmp.setTitle(target.getTitle());
                        LogUtils.i(target.getThumbnail());
                        LogUtils.i(target.getBigThumbnail());
                        tmp.setBigThumbnail(StringUtils.imageUrlFormatter(target.getBigThumbnail()));
                        tmp.setCommentCount(target.getCommentCount());
                        tmp.setTags(target.getTags());
                        User user = new User();
                        user.setId(target.getUser().getId());
                        tmp.setUser(user);
                        videos[i] = tmp;
                    }
                } else {
                    for(int i = 0; i < 5; i++) {
                        videos[i] = new VideoData();
                    }
                }
                bannerData.setVideos(videos);

                res.add(bannerData);
            }

            item.setCategory(categories[res.size() - 1]);
            item.setLogoId(logoIds[res.size() - 1]);

            // 普通类别的视频只需4个 (只用于首页)
            VideoData[] videos = new VideoData[4];
            // 有时候返回为空。。。
            if(item.getVideos() != null && item.getVideos().length == 20) {
                for(int i = 0; i < 4; i++) {
                    videos[i] = item.getVideos()[random.nextInt(5) + 5 * i];
                }
            } else {
                for(int i = 0; i < 4; i++) {
                    videos[i] = new VideoData();
                }
            }
            item.setVideos(videos);
            res.add(item);
        }
        return res;

    }

    @Override
    public void start() {

        Subscription s = dataSource.getAllCategoryData(Arrays.asList(categories),
                new BaseCallback<List<VideosByCategory>>() {
                    @Override
                    public void onDataLoaded(List<VideosByCategory> list) {
                        homeView.showAll(shuffleData(list));
                        if (!NetUtils.isNetworkAvailable())
                            homeView.showNoNetworkData();
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {
                        if (!NetUtils.isNetworkAvailable()) {
                            homeView.showNoNetworkData();
                        } else {
                            homeView.showError(error.toString());
                        }
                        homeView.showNoData();
                    }
                });
        homeView.addViewSubscription(s);
    }

    @Override
    public void loadAll() {

        if(!NetUtils.isNetworkAvailable()) {
            homeView.showNoNetworkData();
            homeView.showNoData();
            return;
        }

        Subscription s = dataSource.getAllCategoryData(Arrays.asList(categories),
                new BaseCallback<List<VideosByCategory>>() {
                    @Override
                    public void onDataLoaded(List<VideosByCategory> list) {
                        homeView.showAll(shuffleData(list));
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
    public void openHotRank() {
        if(!NetUtils.isNetworkAvailable()) {
            homeView.showNoNetworkData();
            return;
        }
        homeView.showHotRank();
    }

    @Override
    public void refreshCategory(final int position, String category) {

        if(!NetUtils.isNetworkAvailable()) {
            homeView.showNoNetworkData();
            homeView.showRefreshCategory(position, null, false);
            return;
        }

        Subscription s = dataSource.getCategoryData(category,
                new BaseCallback<VideosByCategory>() {
                    @Override
                    public void onDataLoaded(VideosByCategory data) {

                        data.setCategory(categories[position]);
                        data.setLogoId(logoIds[position]);

                        VideoData[] videos = new VideoData[4];
                        for(int i = 0; i < 4; i++) {
                            videos[i] = data.getVideos()[random.nextInt(5) + 5 * i];
                        }
                        data.setVideos(videos);

                        homeView.showRefreshCategory(position, data, true);
                    }

                    @Override
                    public void onDataNotAvailable(Error error) {

                        homeView.showError(error.toString());
                        homeView.showRefreshCategory(position, null, false);
                    }
                });
        homeView.addViewSubscription(s);
    }

    @Override
    public void openCategory(String category) {
        if(!NetUtils.isNetworkAvailable()) {
            homeView.showNoNetworkData();
            return;
        }
        homeView.showCategory(category);
    }

    @Override
    public void openVideo(VideoData data) {
        if(!NetUtils.isNetworkAvailable())
            homeView.showNoNetworkData();
        else
            homeView.showWatchVideo(data);
    }
}
