package com.huskyyy.anotheryouku.activity.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/27.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_poster)
    public ImageView posterImageView;
    @BindView(R.id.tv_title)
    public TextView titleTextView;
    @BindView(R.id.tv_name)
    public TextView nameTextView;
    @BindView(R.id.tv_view_count)
    public TextView playCountTextView;
    @BindView(R.id.tv_comment_count)
    public TextView commentCountTextView;
    public VideoViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}