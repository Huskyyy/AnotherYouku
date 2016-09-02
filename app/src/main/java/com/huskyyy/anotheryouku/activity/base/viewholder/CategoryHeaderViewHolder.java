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
public class CategoryHeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_logo)
    public ImageView logoImageView;
    @BindView(R.id.tv_name)
    public TextView nameTextView;
    @BindView(R.id.tv_explore_more)
    public TextView exploreTextView;

    public CategoryHeaderViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
