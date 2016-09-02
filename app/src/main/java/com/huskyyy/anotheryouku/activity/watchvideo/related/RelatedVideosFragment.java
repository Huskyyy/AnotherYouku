package com.huskyyy.anotheryouku.activity.watchvideo.related;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.fragment.BaseFragment;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.ToastUtils;
import com.huskyyy.anotheryouku.activity.watchvideo.WatchVideoActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/6.
 */
public class RelatedVideosFragment extends BaseFragment implements RelatedVideosContract.View {

    private RelatedVideosContract.Presenter presenter;
    private RelatedVideosListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rv, container, false);
        setupRecyclerView((RecyclerView) view.findViewById(R.id.rv));
        presenter.start();
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        adapter = new RelatedVideosListAdapter(getContext());
        adapter.setRelativeVideosItemClickListener(
                new RelatedVideosListAdapter.RelativeVideosItemClickListener() {
                    @Override
                    public void onLoadClick() {
                        adapter.setVideosLoaded(false);
                        adapter.notifyDataSetChanged();
                        presenter.start();
                    }

                    @Override
                    public void onTagClick(String tag) {
                        ToastUtils.showShort(tag);
                    }

                    @Override
                    public void onVideoClick(VideoData videoData) {
                        presenter.openVideo(videoData);
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    ImageLoader.pauseImageLoading();
                } else {
                    ImageLoader.resumeImageLoading();
                }
            }
        });
    }

    @Override
    public void setPresenter(RelatedVideosContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showRelatedVideos(List<String> tags, List<VideoData> videos) {
        adapter.replaceData(tags, videos, true);
    }

    @Override
    public void showVideosWithKey(String key) {
        // 打开搜索关键词界面
    }

    @Override
    public void showWatchVideo(VideoData data) {
        Intent intent = new Intent(getContext(), WatchVideoActivity.class);
        intent.putExtra(WatchVideoActivity.VIDEO_DATA, data);
        intent.putExtra(WatchVideoActivity.USER_DATA, data.getUser());
        startActivity(intent);
    }

    @Override
    public void showNoVideoData(List<String> tags) {
        //ToastUtils.showShort(R.string.related_video_data_unavailable);
        adapter.replaceData(tags, new ArrayList<VideoData>(), false);
    }

    @Override
    public void showNoNetworkData() {
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
