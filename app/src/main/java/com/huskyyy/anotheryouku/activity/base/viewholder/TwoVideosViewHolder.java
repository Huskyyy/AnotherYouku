package com.huskyyy.anotheryouku.activity.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.widget.VideoCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/27.
 */
public class TwoVideosViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cv_video0)
    public VideoCardView videoCardView0;
    @BindView(R.id.cv_video1)
    public VideoCardView videoCardView1;

    public TwoVideosViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
