package com.huskyyy.anotheryouku.activity.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.widget.CommentTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Wang on 2016/8/27.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_avatar)
    public CircleImageView avatarImageView;
    @BindView(R.id.tv_name)
    public TextView nameTextView;
    @BindView(R.id.tv_publish)
    public TextView publishTextView;
    @BindView(R.id.tv_comment)
    public CommentTextView commentTextView;
    @BindView(R.id.view_divider)
    public View dividerView;
    @BindView(R.id.iv_delete)
    public ImageView deleteImageView;
    public CommentViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
