package com.huskyyy.anotheryouku.activity.category.genre;

import android.content.Context;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.DataConstants;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.util.NetUtils;

import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/29.
 */
public class CategoryGenrePresenter implements CategoryGenreContract.Presenter {

    private Context context;
    private DataSource dataSource;
    private CategoryGenreContract.View genreView;
    private String category;
    private String genre;

    public CategoryGenrePresenter(Context context,
                                  DataSource dataSource,
                                  CategoryGenreContract.View genreView,
                                  String category,
                                  String genre) {
        this.context = context;
        this.dataSource = dataSource;
        this.genreView = genreView;
        this.category = category;
        this.genre = genre;
        this.genreView.setPresenter(this);
    }

    private String getOrderParam(String orderBy) {
        if(orderBy.equals(context.getString(R.string.published))) {
            return "published";
        }
        if(orderBy.equals(context.getString(R.string.view_count))) {
            return "view-count";
        }
        if(orderBy.equals(context.getString(R.string.comment_count))) {
            return "comment-count";
        }
        return "favorite-count";
    }

    @Override
    public void start() {
        List<String> tags = DataConstants.videoGenreMap.get(genre);
        genreView.showTags(tags);
        loadData(tags.get(0), context.getString(R.string.published));
    }

    @Override
    public void loadData(final String tag, final String orderBy) {
        if(!NetUtils.isNetworkAvailable()) {
            genreView.showNoNetworkData();
            genreView.showNoData();
            return;
        }
        genreView.clearViewSubscription();
        BaseCallback<VideosByCategory> callback = new BaseCallback<VideosByCategory>() {
            @Override
            public void onDataLoaded(VideosByCategory videosByCategory) {
                videosByCategory.setCategory(category);
                videosByCategory.setGenre(genre);
                videosByCategory.setTag(tag);
                videosByCategory.setOrderBy(orderBy);
                videosByCategory.setPage(1);
                genreView.showData(videosByCategory);
            }

            @Override
            public void onDataNotAvailable(Error error) {
                genreView.showError(error.toString());
                genreView.showNoData();
            }
        };
        Subscription s;
        String orderParam = getOrderParam(orderBy);
        if(tag.equals(context.getString(R.string.all))) {
            s = dataSource.getCategoryData(category, genre, orderParam,
                    1, CategoryGenreAdapter.PAGE_COUNT, callback);
        } else {
            s = dataSource.getVideosByTag(category, tag, orderParam,
                    1, CategoryGenreAdapter.PAGE_COUNT, callback);
        }
        genreView.addViewSubscription(s);
    }

    @Override
    public void loadMore(final String tag, final String orderBy, final int page) {
        if(!NetUtils.isNetworkAvailable()) {
            genreView.showNoNetworkData();
            genreView.showLoadMoreFailed();
            return;
        }
        BaseCallback<VideosByCategory> callback = new BaseCallback<VideosByCategory>() {
            @Override
            public void onDataLoaded(VideosByCategory videosByCategory) {
                videosByCategory.setCategory(category);
                videosByCategory.setGenre(genre);
                videosByCategory.setTag(tag);
                videosByCategory.setOrderBy(orderBy);
                videosByCategory.setPage(page);
                genreView.showLoadMore(videosByCategory);
            }

            @Override
            public void onDataNotAvailable(Error error) {
                genreView.showError(error.toString());
                genreView.showLoadMoreFailed();
            }
        };
        Subscription s;
        String orderParam = getOrderParam(orderBy);
        if(tag.equals(context.getString(R.string.all))) {
            s = dataSource.getCategoryData(category, genre, orderParam,
                    page, CategoryGenreAdapter.PAGE_COUNT, callback);
        } else {
            s = dataSource.getVideosByTag(category, tag, orderParam,
                    page, CategoryGenreAdapter.PAGE_COUNT, callback);
        }
        genreView.addViewSubscription(s);
    }

    @Override
    public void openVideo(VideoData data) {
        if(!NetUtils.isNetworkAvailable()) {
            genreView.showNoNetworkData();
            return;
        }
        genreView.showWatchVideo(data);
    }
}
