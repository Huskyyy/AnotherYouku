package com.huskyyy.anotheryouku.activity.category.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.adapter.SimpleRecyclerViewAdapter;
import com.huskyyy.anotheryouku.activity.base.viewholder.HotHeaderViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadMoreFailedViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadMoreViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadNotificationViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.TwoVideosViewHolder;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Wang on 2016/8/23.
 */
public class CategoryHomeAdapter extends SimpleRecyclerViewAdapter {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_VIDEOS = 2;

    public static final int PAGE_COUNT = 20;

    private Context context;
    private List<VideoData> videos;
    private int currentPage;
    private int totalCount;

    private CategoryHomeItemClickListener listener;

    public CategoryHomeAdapter(Context context) {
        this.context = context;
        this.videos = new ArrayList<>();
        reset();
        setShowLoadMore(true);
    }

    public void replaceData(VideosByCategory videos, boolean loadSucceed) {

        if(loadSucceed) {
            if(videos.getVideos() != null && videos.getVideos().length != 0) {
                this.videos = new ArrayList<>(Arrays.asList(videos.getVideos()));
            } else {
                this.videos = new ArrayList<>();
            }
            currentPage = videos.getPage();
            totalCount = videos.getTotal();
            LogUtils.i("total " + totalCount);
            setDataLoadSucceed(true);
            setDataLoaded(true);
            notifyDataSetChanged();
        } else if(getBaseItemCount() == 0) {
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
        return videos.size() == 0 ? 0 : (videos.size() + 1) / 2 + 1;
    }

    @Override
    protected int getBaseItemTotalCount() {
        return totalCount == 0 ? 0 : (totalCount + 1) / 2 + 1;
    }

    @Override
    protected int getBaseItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_VIDEOS;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_hot_header, parent, false);
                HotHeaderViewHolder holder = new HotHeaderViewHolder(view);
                initHeader(holder);
                return holder;
            case TYPE_VIDEOS:
            default:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_2videos, parent, false);
                TwoVideosViewHolder holder1 = new TwoVideosViewHolder(view);
                initVideosClick(holder1);
                return holder1;
        }
    }


    @Override
    protected void onBindBaseViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position > 0) {
            TwoVideosViewHolder viewHolder = (TwoVideosViewHolder) holder;
            initVideos(viewHolder, position);
        }
    }

    private void initHeader(HotHeaderViewHolder viewHolder) {
        viewHolder.logoImageView.setImageResource(R.drawable.ic_fiber_new_black_48dp);
        viewHolder.nameTextView.setText(R.string.category_news);
        final View.OnClickListener li = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onHotRankClick();
                }
            }
        };
        viewHolder.exploreTextView.setOnClickListener(li);
    }

    private void initVideosClick(final TwoVideosViewHolder viewHolder) {
        viewHolder.videoCardView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    int position = viewHolder.getAdapterPosition() - 1;
                    if(position * 2 < videos.size()) {
                        listener.onVideoClick(videos.get(position * 2));
                    }
                }
            }
        });

        viewHolder.videoCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    int position = viewHolder.getAdapterPosition() - 1;
                    if(position * 2 + 1 < videos.size()) {
                        listener.onVideoClick(videos.get(position * 2 + 1));
                    }
                }
            }
        });
    }

    private void initVideos(TwoVideosViewHolder viewHolder, int position) {
        int dataPos0 = (position - 1) * 2;
        int dataPos1 = (position - 1) * 2 + 1;
        if(dataPos0 < videos.size()) {
            viewHolder.videoCardView0.setVisibility(View.VISIBLE);
            viewHolder.videoCardView0.setVideo(videos.get(dataPos0));
        } else {
            viewHolder.videoCardView0.setVisibility(View.INVISIBLE);
        }
        if(dataPos1 < videos.size()) {
            viewHolder.videoCardView1.setVisibility(View.VISIBLE);
            viewHolder.videoCardView1.setVideo(videos.get(dataPos1));
        } else {
            viewHolder.videoCardView1.setVisibility(View.INVISIBLE);
        }
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

    @Override
    protected void onBindNoDataViewHolder(RecyclerView.ViewHolder viewHolder) {
    }

    @Override
    protected void onBindLoadFailedViewHolder(RecyclerView.ViewHolder viewHolder) {
    }

    @Override
    protected void onBindLoadMoreViewHolder(RecyclerView.ViewHolder viewHolder) {
    }

    @Override
    protected void onBindLoadMoreFailedViewHolder(RecyclerView.ViewHolder viewHolder) {
    }



    public void setCategoryHomeItemClickListener (CategoryHomeItemClickListener listener) {
        this.listener = listener;
    }

    public interface CategoryHomeItemClickListener {

        void onLoadClick();

        void onHotRankClick();

        void onVideoClick(VideoData data);

        void onLoadMoreClick(int page);


    }
}
