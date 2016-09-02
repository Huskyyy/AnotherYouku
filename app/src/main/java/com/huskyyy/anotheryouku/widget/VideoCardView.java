package com.huskyyy.anotheryouku.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.StringUtils;

/**
 * Created by Wang on 2016/8/24.
 */
public class VideoCardView extends CardView {

    RatioImageView thumbnailImageView;
    TextView titleTextView;
    TextView playCountTextView;
    TextView commentCountTextView;

    public VideoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_video_card, this);
        thumbnailImageView = (RatioImageView) findViewById(R.id.iv_thumbnail);
        titleTextView = (TextView) findViewById(R.id.tv_title);
        playCountTextView = (TextView) findViewById(R.id.tv_view_count);
        commentCountTextView = (TextView) findViewById(R.id.tv_comment_count);
    }

    public void setVideo(VideoData data) {
        ImageLoader.loadNormalImage(data.getThumbnail(), thumbnailImageView);
        titleTextView.setText(data.getTitle());
        playCountTextView.setText(
                StringUtils.numberFormatter(data.getViewCount()));
        commentCountTextView.setText(
                StringUtils.numberFormatter(data.getCommentCount()));
    }
}
