package com.huskyyy.anotheryouku.activity.base.viewholder;

import android.support.v7.widget.CardView;
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
public class CategoryFooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cv_show_more)
    public CardView showMoreCardView;
    @BindView(R.id.tv_show_more)
    public TextView showMoreTextView;
    @BindView(R.id.tv_refresh)
    public TextView refreshTextView;
    @BindView(R.id.iv_refresh)
    public ImageView refreshImageView;

    public CategoryFooterViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
