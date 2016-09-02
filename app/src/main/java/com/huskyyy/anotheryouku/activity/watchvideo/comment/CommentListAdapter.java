package com.huskyyy.anotheryouku.activity.watchvideo.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.adapter.SimpleRecyclerViewAdapter;
import com.huskyyy.anotheryouku.activity.base.viewholder.CommentViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadNotificationViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.MoreCommentViewHolder;
import com.huskyyy.anotheryouku.data.base.Comment;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.widget.CommentTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang on 2016/8/26.
 */
public class CommentListAdapter extends SimpleRecyclerViewAdapter {

    private static final int TYPE_HOT_COMMENT = 1;
    private static final int TYPE_NO_MORE_HOT_COMMENT = 2;
    private static final int TYPE_MORE_HOT_COMMENT = 3;
    private static final int TYPE_COMMENT = 4;
    private static final int TYPE_NO_MORE_COMMENT = 5;
    private static final int TYPE_MORE_COMMENT = 6;

    private static final int HOT_COMMENT_NUM = 3;
    private static final int NORMAL_COMMENT_NUM = 10;

    private Context context;
    private RecyclerView recyclerView;
    private List<Comment> hotComments;
    private List<Comment> comments;
    private int hotCommentsSize;
    private int commentsSize;
    private boolean moreHotComments;
    private boolean moreComments;
    private CommentItemClickListener listener;

    public CommentListAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.hotComments = new ArrayList<>();
        this.comments = new ArrayList<>();
        reset();
        setShowLoading(true);
        UpdateSizeFlag();
    }

    private void UpdateSizeFlag(){

        if(!isDataLoaded() || !isDataLoadSucceed()) {
            hotCommentsSize = hotComments.size();
            commentsSize = comments.size();
            if(hotCommentsSize > HOT_COMMENT_NUM) {
                moreHotComments = true;
                hotCommentsSize = HOT_COMMENT_NUM;
            }
            if(commentsSize > NORMAL_COMMENT_NUM) {
                moreComments = true;
                commentsSize = NORMAL_COMMENT_NUM;
            }
        }
    }

    public void addComment(Comment comment) {
        if(hotCommentsSize + commentsSize == 0) {
            comments.add(0, comment);
            commentsSize++;
            notifyDataSetChanged();
        } else {
            comments.add(0, comment);
            commentsSize++;
            int pos;
            if(hotCommentsSize == 0) {
                pos = 0;
            } else {
                pos = hotCommentsSize + 1;
            }
            notifyItemInserted(pos);
            recyclerView.smoothScrollToPosition(pos);
        }
    }


    public void removeComment(Comment comment) {
        // 通常自己的评论是从头部加入的，因此直接搜索
        int pos = comments.indexOf(comment);
        comments.remove(comment);
        commentsSize--;
        if(hotCommentsSize != 0) {
            pos += hotCommentsSize + 1;
        }
        if(hotCommentsSize + commentsSize == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(pos);
            if(pos == getBaseItemCount() - 1) {
                notifyItemChanged(pos - 1);
            }
        }
    }

    public void replaceComment(List<Comment> comments, boolean loadSucceed) {
        if(loadSucceed) {
            this.comments = comments;
            UpdateSizeFlag();
            setDataLoadSucceed(true);
        }
        setDataLoaded(true);
        notifyDataSetChanged();
    }

    public void replaceHotComment(List<Comment> hotComments) {
        this.hotComments = hotComments;
        UpdateSizeFlag();
        notifyDataSetChanged();
    }

    @Override
    protected int getBaseItemCount() {
        int res = 0;
        res += (hotCommentsSize > 0 ? hotCommentsSize + 1 : 0);
        res += (commentsSize > 0 ? commentsSize + 1 : 0);
        return res;
    }

    @Override
    protected int getBaseItemTotalCount() {
        return getBaseItemCount();
    }

    @Override
    protected int getBaseItemViewType(int position) {
        if(hotCommentsSize == 0) {
            if(position < commentsSize) {
                return TYPE_COMMENT;
            } else if(moreComments) {
                return TYPE_MORE_COMMENT;
            } else {
                return TYPE_NO_MORE_COMMENT;
            }
        } else {
            if(position < hotCommentsSize) {
                return TYPE_HOT_COMMENT;
            } else if(position == hotCommentsSize) {
                if(moreHotComments) {
                    return TYPE_MORE_HOT_COMMENT;
                } else {
                    return TYPE_NO_MORE_HOT_COMMENT;
                }
            } else if(position < hotCommentsSize + commentsSize + 1) {
                return TYPE_COMMENT;
            } else if(moreComments) {
                return TYPE_MORE_COMMENT;
            } else {
                return TYPE_NO_MORE_COMMENT;
            }
        }
    }


    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_COMMENT:
            case TYPE_HOT_COMMENT:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_comment, parent, false);
                CommentViewHolder holder = new CommentViewHolder(view);
                initCommentClick(holder);
                return holder;
            case TYPE_MORE_HOT_COMMENT:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_more_comment, parent, false);
                MoreCommentViewHolder holder1 = new MoreCommentViewHolder(view);
                initMoreHotCommentClick(holder1);
                return holder1;
            case TYPE_MORE_COMMENT:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_more_comment, parent, false);
                MoreCommentViewHolder holder3 = new MoreCommentViewHolder(view);
                initMoreCommentClick(holder3);
                return holder3;
            case TYPE_NO_MORE_HOT_COMMENT:
            case TYPE_NO_MORE_COMMENT:
            default:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_more_comment, parent, false);
                return new MoreCommentViewHolder(view);
        }
    }

    @Override
    protected void onBindBaseViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(hotCommentsSize == 0) {
            if(position < commentsSize) {
                Comment comment = comments.get(position);
                CommentViewHolder viewHolder = (CommentViewHolder) holder;
                initComment(comment, viewHolder, position < commentsSize - 1);
            } else {
                MoreCommentViewHolder viewHolder = (MoreCommentViewHolder) holder;
                if(moreComments) {
                    initMoreComment(viewHolder);
                } else {
                    initNoMoreComment(viewHolder);
                }
            }
        } else {
            if(position < hotCommentsSize) {
                Comment comment = hotComments.get(position);
                CommentViewHolder viewHolder = (CommentViewHolder) holder;
                initComment(comment, viewHolder, position < hotCommentsSize - 1);
            } else if(position == hotCommentsSize) {
                MoreCommentViewHolder viewHolder = (MoreCommentViewHolder) holder;
                if(moreHotComments) {
                    initMoreHotComment(viewHolder);
                } else {
                    initNoMoreHotComment(viewHolder);
                }
            } else if(position < hotCommentsSize + commentsSize + 1) {
                Comment comment = comments.get(position - hotCommentsSize - 1);
                CommentViewHolder viewHolder = (CommentViewHolder) holder;
                initComment(comment, viewHolder, position < hotCommentsSize + commentsSize);
            } else{
                MoreCommentViewHolder viewHolder = (MoreCommentViewHolder) holder;
                if(moreComments) {
                    initMoreComment(viewHolder);
                } else {
                    initNoMoreComment(viewHolder);
                }
            }
        }
    }


    @Override
    protected RecyclerView.ViewHolder getLoadFailedViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_load_notification, parent, false);
        LoadNotificationViewHolder holder = new LoadNotificationViewHolder(view);
        initLoadFailed(holder);
        return holder;
    }

    @Override
    protected RecyclerView.ViewHolder getLoadingViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_load_notification, parent, false);
        LoadNotificationViewHolder holder = new LoadNotificationViewHolder(view);
        initLoading(holder);
        return holder;
    }

    @Override
    protected RecyclerView.ViewHolder getNoDataViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_load_notification, parent, false);
        LoadNotificationViewHolder holder = new LoadNotificationViewHolder(view);
        initNoData(holder);
        return holder;
    }

    @Override
    protected void onBindLoadFailedViewHolder(RecyclerView.ViewHolder viewHolder) {
    }

    @Override
    protected void onBindLoadingViewHolder(RecyclerView.ViewHolder viewHolder) {
    }

    @Override
    protected void onBindNoDataViewHolder(RecyclerView.ViewHolder viewHolder) {
    }

    private void initCommentClick(final CommentViewHolder viewHolder) {

        viewHolder.avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    Comment comment = getCommentByViewHolder(viewHolder);
                    listener.onUserClick(comment.getUser().getId(), comment.getUser().getName());
                }
            }
        });
        viewHolder.commentTextView.setNameClickListener(new CommentTextView.NameClickListener() {
            @Override
            public void onNameClickListener(String name) {
                if(listener != null) {
                    listener.onUserClick(-1, name);
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    Comment comment = getCommentByViewHolder(viewHolder);
                    listener.onCommentClick(comment);
                }
            }
        });

        viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (listener != null) {
                    Comment comment = getCommentByViewHolder(viewHolder);
                    LogUtils.i(comment.getUser().getName());
                    listener.onDeleteCommentClick(comment);
                }
            }
        });

    }

    private void initComment(final Comment comment, CommentViewHolder viewHolder,
                             boolean withBorder) {

        ImageLoader.loadAvatarImage(comment.getUser().getAvatar(), viewHolder.avatarImageView);
        viewHolder.nameTextView.setText(comment.getUser().getName());
        viewHolder.publishTextView.setText(comment.getPublished());
        viewHolder.commentTextView.setComment(comment.getContent());
        if(comment.isSendByAccount()) {
            viewHolder.deleteImageView.setVisibility(View.VISIBLE);
            viewHolder.deleteImageView.setEnabled(true);
        } else {
            viewHolder.deleteImageView.setVisibility(View.GONE);
            viewHolder.deleteImageView.setEnabled(false);
        }
        if(!withBorder) {
            viewHolder.dividerView.setVisibility(View.GONE);
        } else {
            viewHolder.dividerView.setVisibility(View.VISIBLE);
        }
    }

    private void initMoreCommentClick(final MoreCommentViewHolder viewHolder) {
        viewHolder.moreCommentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null  && moreComments
                        && viewHolder.getAdapterPosition() == getBaseItemCount() - 1) {
                    listener.onMoreCommentsClick();
                }
            }
        });
    }

    private void initMoreComment(MoreCommentViewHolder viewHolder) {
        viewHolder.moreCommentTextView.setText(R.string.more_comment);
        viewHolder.moreCommentTextView.setEnabled(true);
    }

    private void initMoreHotCommentClick(final MoreCommentViewHolder viewHolder) {
        viewHolder.moreCommentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null && moreHotComments
                        && viewHolder.getAdapterPosition() == hotCommentsSize) {
                    listener.onMoreHotCommentsClick();
                }
            }
        });
    }

    private void initMoreHotComment(MoreCommentViewHolder viewHolder) {
        viewHolder.moreCommentTextView.setText(R.string.more_hot_comment);
        viewHolder.moreCommentTextView.setEnabled(true);
    }

    private void initNoMoreComment(MoreCommentViewHolder viewHolder) {
        viewHolder.moreCommentTextView.setText(R.string.no_more_comment);
        viewHolder.moreCommentTextView.setEnabled(false);
    }

    private void initNoMoreHotComment(MoreCommentViewHolder viewHolder) {
        viewHolder.moreCommentTextView.setText(R.string.no_more_hot_comment);
        viewHolder.moreCommentTextView.setEnabled(false);
    }

    private void initLoading(LoadNotificationViewHolder viewHolder) {
        viewHolder.notificationTextView.setText(R.string.loading_comment);
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
        viewHolder.notificationTextView.setText(R.string.no_comment_data);
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


    private Comment getCommentByViewHolder(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        Comment comment;
        if(hotCommentsSize == 0) {
            comment = comments.get(position);
        } else {
            if(position < hotCommentsSize) {
                comment = hotComments.get(position);
            } else {
                comment = comments.get(position - hotCommentsSize - 1);
            }
        }
        return comment;
    }

    public void setItemClickListener(CommentItemClickListener listener) {
        this.listener = listener;
    }

    public interface CommentItemClickListener {

        void onMoreHotCommentsClick();

        void onMoreCommentsClick();

        void onCommentClick(Comment comment);

        void onDeleteCommentClick(Comment comment);

        void onUserClick(long id, String name);

        void onLoadClick();
    }
}
