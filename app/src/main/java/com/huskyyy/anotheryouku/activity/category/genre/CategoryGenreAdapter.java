package com.huskyyy.anotheryouku.activity.category.genre;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.adapter.SimpleRecyclerViewAdapter;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadMoreFailedViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadMoreViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadNotificationViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadingViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.VideoViewHolder;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.util.StringUtils;
import com.youku.player.ui.widget.Loading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/30.
 */
public class CategoryGenreAdapter extends SimpleRecyclerViewAdapter {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_VIDEO = 2;

    public static final int PAGE_COUNT = 10;

    private Context context;
    private List<VideoData> videos;
    private int currentPage;
    private int totalCount;
    private String orderBy;

    private CategoryGenreItemClickListener listener;

    public CategoryGenreAdapter(Context context) {
        this.context = context;
        this.videos = new ArrayList<>();
        reset();
        setShowLoading(true);
        setShowLoadMore(true);
    }

    public void replaceData(VideosByCategory videos, boolean loadSucceed) {

        if(loadSucceed) {
            if(videos.getVideos() != null && videos.getVideos().length != 0) {
                this.videos.clear();
                this.videos.addAll(Arrays.asList(videos.getVideos()));
            } else {
                this.videos.clear();
            }
            currentPage = videos.getPage();
            totalCount = videos.getTotal();
            orderBy = videos.getOrderBy();
            setDataLoadSucceed(true);
            setDataLoaded(true);
            notifyDataSetChanged();
        } else {
            this.videos.clear();
            currentPage = 0;
            totalCount = 0;
            setDataLoaded(true);
            notifyDataSetChanged();
        }
    }

    public void loadMoreData(VideosByCategory videos, boolean loadMoreSucceed) {

        int len = 0;
        if(loadMoreSucceed) {
            if(videos.getVideos() != null) {
                len = videos.getVideos().length;
                for(VideoData data : videos.getVideos()) {
                    this.videos.add(data);
                }
            }
            if(len != 0) {
                currentPage = videos.getPage();
            }
            if(this.videos.size() >= totalCount || len < PAGE_COUNT) { // 全部加载完了
                totalCount = this.videos.size();
                LogUtils.i("total " + totalCount);
            }
        }
        setMoreDataLoadSucceed(loadMoreSucceed);
        if(len != 0) {
            notifyItemRangeInserted(getBaseItemCount(), len);
        }
        notifyBottomHolderChanged();
        finishLoadMore();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    protected int getBaseItemCount() {
        return videos.size() == 0 ? 0 : videos.size() + 1;
    }

    @Override
    protected int getBaseItemTotalCount() {
        return totalCount == 0 ? 0 : totalCount + 1;
    }

    @Override
    protected int getBaseItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_VIDEO;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_category_genre_header, parent, false);
                GenreHeaderViewHolder holder = new GenreHeaderViewHolder(view);
                return holder;
            case TYPE_VIDEO:
            default:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_video, parent, false);
                VideoViewHolder holder1 = new VideoViewHolder(view);
                initVideoClick(holder1);
                return holder1;
        }
    }

    @Override
    protected void onBindBaseViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            GenreHeaderViewHolder viewHolder = (GenreHeaderViewHolder) holder;
            initHeader(viewHolder);
        } else {
            VideoViewHolder viewHolder = (VideoViewHolder) holder;
            initVideo(viewHolder, position);
        }
    }


    private void initHeader(GenreHeaderViewHolder viewHolder) {
        viewHolder.orderTextView.setText(
                StringUtils.orderFormatter(context, orderBy));
    }

    private void initVideoClick(final VideoViewHolder viewHolder) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    int dataPos = viewHolder.getAdapterPosition() - 1;
                    listener.onVideoClick(videos.get(dataPos));
                }
            }
        });
    }

    private void initVideo(VideoViewHolder viewHolder, int position) {
        int dataPos = position - 1;
        final VideoData videoData = videos.get(dataPos);
        ImageLoader.loadNormalImage(videoData.getThumbnail(), viewHolder.posterImageView);
        viewHolder.titleTextView.setText(videoData.getTitle());
        viewHolder.nameTextView.setText(videoData.getUser().getName());
        viewHolder.playCountTextView.setText(
                StringUtils.numberFormatter(videoData.getViewCount()));
        viewHolder.commentCountTextView.setText(
                StringUtils.numberFormatter(videoData.getCommentCount()));
    }

    @Override
    protected RecyclerView.ViewHolder getLoadingViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_loading, parent, false);
        LoadingViewHolder holder = new LoadingViewHolder(view);
        initLoading(holder);
        return holder;
    }

    private void initLoading(LoadingViewHolder viewHolder) {
        viewHolder.progressBar.setBarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    @Override
    protected void onBindLoadingViewHolder(RecyclerView.ViewHolder viewHolder) {
        LoadingViewHolder holder = (LoadingViewHolder) viewHolder;
        holder.progressBar.spin();
    }

    @Override
    protected RecyclerView.ViewHolder getNoDataViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_load_notification, parent, false);
        LoadNotificationViewHolder holder = new LoadNotificationViewHolder(view);
        initNoData(holder);
        return holder;
    }

    private void initNoData(LoadNotificationViewHolder viewHolder) {
        viewHolder.notificationTextView.setText(R.string.no_video_data);
        viewHolder.loadTextView.setText(R.string.refresh);
        viewHolder.loadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onLoadClick();
                }
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getLoadFailedViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_load_notification, parent, false);
        LoadNotificationViewHolder holder = new LoadNotificationViewHolder(view);
        initLoadFailed(holder);
        return holder;
    }

    private void initLoadFailed(LoadNotificationViewHolder viewHolder) {
        viewHolder.notificationTextView.setText(R.string.load_failed);
        viewHolder.loadTextView.setText(R.string.reload);
        viewHolder.loadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onLoadClick();
                }
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getLoadMoreViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_load_more, parent, false);
        LoadMoreViewHolder holder = new LoadMoreViewHolder(view);
        initLoadMore(holder);
        return holder;
    }

    private void initLoadMore(LoadMoreViewHolder viewHolder) {
        viewHolder.progressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected RecyclerView.ViewHolder getLoadMoreFailedViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_load_more_failed, parent, false);
        LoadMoreFailedViewHolder holder = new LoadMoreFailedViewHolder(view);
        initLoadMoreFailed(holder);
        return holder;
    }

    private void initLoadMoreFailed(LoadMoreFailedViewHolder viewHolder) {
        View.OnClickListener li = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onLoadMoreClick(currentPage + 1);
                }
            }
        };
        viewHolder.itemView.setOnClickListener(li);
        viewHolder.retryTextView.setOnClickListener(li);
    }


    public static class GenreHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order)
        TextView orderTextView;
        public GenreHeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setCategoryGenreItemClickListener(CategoryGenreItemClickListener listener) {
        this.listener = listener;
    }

    public interface CategoryGenreItemClickListener {

        void onLoadClick();

        void onVideoClick(VideoData data);

        void onLoadMoreClick(int page);

    }
}
