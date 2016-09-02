package com.huskyyy.anotheryouku.activity.category.home;

import android.content.Context;
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
import com.huskyyy.anotheryouku.activity.watchvideo.WatchVideoActivity;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by Wang on 2016/8/28.
 */
public class CategoryHomeFragment extends SwipeRefreshFragment implements CategoryHomeContract.View {

    private static final int PRELOAD_SIZE = 6;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    private CategoryHomeContract.Presenter presenter;

    private CategoryHomeAdapter adapter;

    public CategoryHomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rv_swipe_scrollbar, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onDestroyView() {
        clearSubscription();
        stopRefreshing();
        super.onDestroyView();
    }

    private void setupRecyclerView() {
        adapter = new CategoryHomeAdapter(this.getContext());
        adapter.setCategoryHomeItemClickListener(new CategoryHomeAdapter.CategoryHomeItemClickListener() {
            @Override
            public void onLoadClick() {
                adapter.prepareLoad(false);
                startRefreshing();
                presenter.start();
            }

            @Override
            public void onHotRankClick() {
                presenter.openHotRank();
            }

            @Override
            public void onVideoClick(VideoData data) {
                presenter.openVideo(data);
            }

            @Override
            public void onLoadMoreClick(int page) {
                adapter.prepareLoadMore();
                presenter.loadMore(page);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
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

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {

                // 自动加载要符合以下条件：
                // 1.数据已加载成功
                // 2.目前不在加载状态
                // 3.若上次加载失败则不自动加载
                // 4.有更多数据可以加载
                // 5.即将到达数据底部
                if (adapter.isDataLoadSucceed() && adapter.getBaseItemCount() != 0
                        && !adapter.isLoadingMore() && adapter.isMoreDataLoadSucceed()
                        && adapter.getBaseItemCount() < adapter.getBaseItemTotalCount()) {
                    boolean startLoadMore = layoutManager.findLastCompletelyVisibleItemPosition() >
                            adapter.getBaseItemCount() - PRELOAD_SIZE;
                    if(startLoadMore) {
                        LogUtils.i("load page " + (adapter.getCurrentPage() + 1));
                        adapter.prepareLoadMore();
                        presenter.loadMore(adapter.getCurrentPage() + 1);
                    }
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
        presenter.start();
    }

    @Override
    protected boolean isDataInitialized() {
        return adapter.isDataLoaded();
    }


    @Override
    public void setPresenter(CategoryHomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAll(VideosByCategory videos) {
        stopRefreshing();
        adapter.replaceData(videos, true);
    }

    @Override
    public void showHotRank() {
        // 打开排行榜
        ToastUtils.showShort("Open hot rank.");
    }

    @Override
    public void showWatchVideo(VideoData data) {
        Intent intent = new Intent(getContext(), WatchVideoActivity.class);
        intent.putExtra(WatchVideoActivity.VIDEO_DATA, data);
        intent.putExtra(WatchVideoActivity.USER_DATA, data.getUser());
        startActivity(intent);
    }

    @Override
    public void showLoadMore(VideosByCategory videos) {
        adapter.loadMoreData(videos, true);
    }

    @Override
    public void showLoadMoreFailed() {
        adapter.loadMoreData(null, false);
    }

    @Override
    public void showNoData() {
        stopRefreshing();
        adapter.replaceData(null, false);
    }

    @Override
    public void showNoNetworkData() {
        stopRefreshing();
        ToastUtils.showShort(R.string.network_unavailable);
    }

    @Override
    public void showError(String error) {
        ToastUtils.showShort(error);
    }

    @Override
    public void addViewSubscription(Subscription subscription) {
        addSubscription(subscription);
    }
}
