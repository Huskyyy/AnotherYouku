package com.huskyyy.anotheryouku.activity.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.huskyyy.anotheryouku.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/27.
 */
public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progressbar)
    public ProgressBar progressBar;
    public LoadMoreViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
