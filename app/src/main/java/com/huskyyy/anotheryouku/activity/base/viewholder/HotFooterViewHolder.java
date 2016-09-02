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
public class HotFooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_refresh)
    public TextView refreshTextView;
    @BindView(R.id.iv_refresh)
    public ImageView refreshImageView;

    public HotFooterViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
