package com.huskyyy.anotheryouku.activity.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.fragment.SwipeRefreshFragment;
import com.huskyyy.anotheryouku.activity.category.CategoryActivity;
import com.huskyyy.anotheryouku.activity.main.home.adapter.HomeListAdapter;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.ToastUtils;
import com.huskyyy.anotheryouku.activity.watchvideo.WatchVideoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by Wang on 2016/7/23.
 */
public class HomeFragment extends SwipeRefreshFragment implements HomeContract.View {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    private HomeContract.Presenter presenter;

    private HomeListAdapter adapter;

    public HomeFragment() {}

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null) {
            adapter.enableBannerScroll(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(adapter != null) {
            adapter.enableBannerScroll(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rv_swipe, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView(recyclerView);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        adapter = new HomeListAdapter(this.getContext());
        adapter.setHomeItemClickListener(new HomeListAdapter.HomeItemClickListener() {

            @Override
            public void onLoadClick() {
                adapter.prepareLoad(false);
                startRefreshing();
                presenter.loadAll();
            }

            @Override
            public void onBannerClick(VideoData data) {
                presenter.openVideo(data);
            }

            @Override
            public void onHotRankClick(){
                presenter.openHotRank();
            }

            @Override
            public void onCategoryClick(String category) {
                presenter.openCategory(category);
            }

            @Override
            public void onCategoryRefreshClick(int position, String category) {
                presenter.refreshCategory(position, category);
            }

            @Override
            public void onVideoClick(VideoData data) {
                presenter.openVideo(data);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState != RecyclerView.SCROLL_STATE_IDLE) {
                    ImageLoader.pauseImageLoading();
                } else {
                    ImageLoader.resumeImageLoading();
                }
            }
        });
    }

    @Override
    protected void initData() {
        presenter.start();
    }

    @Override
    protected void loadData() {
        adapter.prepareLoad(false);
        presenter.loadAll();
    }

    @Override
    protected boolean isDataInitialized() {
        return adapter.isDataLoaded();
    }



    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAll(List<VideosByCategory> list) {
        stopRefreshing();
        adapter.replaceAllData(list, true);
    }

    @Override
    public void showHotRank() {
        ToastUtils.showShort("open rank");
    }

    @Override
    public void showRefreshCategory(int position, VideosByCategory videos, boolean refreshed) {
        adapter.replaceCategoryData(position, videos, refreshed);
    }

    @Override
    public void showCategory(String category) {
        Intent intent = new Intent(getContext(), CategoryActivity.class);
        intent.putExtra(CategoryActivity.CATEGORY, category);
        startActivity(intent);
    }

    @Override
    public void showWatchVideo(VideoData data) {
        Intent intent = new Intent(getContext(), WatchVideoActivity.class);
        intent.putExtra(WatchVideoActivity.VIDEO_DATA, data);
        intent.putExtra(WatchVideoActivity.USER_DATA, data.getUser());
        startActivity(intent);
    }

    @Override
    public void showNoNetworkData() {
        stopRefreshing();
        ToastUtils.showShort(R.string.network_unavailable);
    }

    @Override
    public void showNoData() {
        stopRefreshing();
        //ToastUtils.showShort(R.string.video_data_unavailable);
        adapter.replaceAllData(null, false);
    }

    @Override
    public void showError(String error) {
        stopRefreshing();
        ToastUtils.showShort(error);
    }

    @Override
    public void addViewSubscription(Subscription subscription) {
        addSubscription(subscription);
    }

}
