package com.huskyyy.anotheryouku.activity.watchvideo.related;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadNotificationViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.TagsViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.VideoViewHolder;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.StringUtils;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang on 2016/8/6.
 */
public class RelatedVideosListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TAG = 1;
    private static final int TYPE_VIDEO = 2;
    private static final int TYPE_LOADING = 3;
    private static final int TYPE_LOAD_FAILED = 4;
    private static final int TYPE_NO_DATA = 5;

    private Context context;
    private List<String> tags;
    private List<VideoData> videos;

    private RelativeVideosItemClickListener listener;

    private boolean tagsLoaded;
    private boolean videosLoaded;
    // 用于表示相关视频信息是否加载成功
    private boolean dataLoadSucceed;

    public RelatedVideosListAdapter(Context context) {
        this.context = context;
        this.tags = new ArrayList<>();
        this.videos = new ArrayList<>();
        reset();
    }

    public void reset() {
        tagsLoaded = false;
        videosLoaded = false;
        dataLoadSucceed = false;
    }

    public void setVideosLoaded(boolean b) {
        videosLoaded = b;
    }

    /**
     *
     * @param tags
     * @param videos
     */
    public void replaceData(List<String> tags, List<VideoData> videos, boolean dataLoadSucceed) {
        reset();
        this.tags = tags;
        this.videos = videos;
        tagsLoaded = true;
        videosLoaded = true;
        this.dataLoadSucceed = dataLoadSucceed;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if(!tagsLoaded && !videosLoaded) { // 加载中
            return 1;
        }
        if(videos.size() == 0) {
            return tags.size() == 0 ? 1 : 2;
        }

        return tags.size() == 0 ? videos.size() : videos.size() + 1;

    }

    @Override
    public int getItemViewType(int position) {

        if(!tagsLoaded && !videosLoaded) {
            return TYPE_LOADING;
        }
        if(tagsLoaded && !videosLoaded) {
            if(tags.size() != 0 && position == 0) {
                return TYPE_TAG;
            } else {
                return TYPE_LOADING;
            }
        }
        if(videos.size() == 0) {
            if(tags.size() != 0 && position == 0) {
                return TYPE_TAG;
            }
            if(dataLoadSucceed) {
                return TYPE_NO_DATA;
            } else {
                return TYPE_LOAD_FAILED;
            }
        }
        if(tags.size() != 0 && position == 0) {
            return TYPE_TAG;
        }
        return TYPE_VIDEO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case TYPE_TAG:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_tags, parent, false);
                TagsViewHolder holder = new TagsViewHolder(view);
                initTags(holder);
                return holder;
            case TYPE_VIDEO:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_video, parent, false);
                VideoViewHolder holder1 = new VideoViewHolder(view);
                initVideoClick(holder1);
                return holder1;
            case TYPE_LOADING:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.view_load_notification, parent, false);
                LoadNotificationViewHolder holder2 = new LoadNotificationViewHolder(view);
                initLoading(holder2);
                return holder2;
            case TYPE_NO_DATA:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.view_load_notification, parent, false);
                LoadNotificationViewHolder holder3 = new LoadNotificationViewHolder(view);
                initNoData(holder3);
                return holder3;
            case TYPE_LOAD_FAILED:
            default:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.view_load_notification, parent, false);
                LoadNotificationViewHolder holder4 = new LoadNotificationViewHolder(view);
                initLoadFailed(holder4);
                return holder4;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(!tagsLoaded && !videosLoaded) {

        } else if(tagsLoaded && !videosLoaded) {
            if(tags.size() != 0 && position == 0) {

            } else {

            }
        } else if(videos.size() == 0) {
            if(tags.size() != 0 && position == 0) {

            } else {

            }
        } else if(tags.size() != 0 && position == 0) {

        } else {
            VideoViewHolder viewHolder = (VideoViewHolder) holder;
            initVideo(viewHolder, position);
        }
    }

    private void initTags(TagsViewHolder viewHolder) {
        FlowLayout tagsFlow = viewHolder.tagsFlow;
        for(int i = 0; i < tags.size(); i++) {
            final String tag = tags.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.view_tag, tagsFlow, false);
            TextView textView = (TextView) view.findViewById(R.id.tv_tag);
            textView.setText(tag);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onTagClick(tag);
                    }
                }
            });
            tagsFlow.addView(view);
        }
    }

    private void initVideoClick(final VideoViewHolder viewHolder) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    int dataPos;
                    if(tags.size() == 0) {
                        dataPos = viewHolder.getAdapterPosition();
                    } else {
                        dataPos = viewHolder.getAdapterPosition() - 1;
                    }
                    listener.onVideoClick(videos.get(dataPos));
                }
            }
        });
    }

    private void initVideo(VideoViewHolder viewHolder, int position) {
        int dataPos;
        if(tags.size() == 0) {
            dataPos = position;
        } else {
            dataPos = position - 1;
        }
        final VideoData videoData = videos.get(dataPos);
        ImageLoader.loadNormalImage(videoData.getThumbnail(), viewHolder.posterImageView);
        viewHolder.titleTextView.setText(videoData.getTitle());
        viewHolder.nameTextView.setText(videoData.getUser().getName());
        viewHolder.playCountTextView.setText(
                StringUtils.numberFormatter(videoData.getViewCount()));
        viewHolder.commentCountTextView.setText(
                StringUtils.numberFormatter(videoData.getCommentCount()));
    }

    private void initLoading(LoadNotificationViewHolder viewHolder) {
        viewHolder.notificationTextView.setText(R.string.loading_related_videos);
        viewHolder.loadTextView.setVisibility(View.GONE);
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

    public void setRelativeVideosItemClickListener(RelativeVideosItemClickListener listener) {
        this.listener = listener;
    }

    public interface RelativeVideosItemClickListener {

        void onLoadClick();

        void onTagClick(String tag);

        void onVideoClick(VideoData videoData);
    }


}
