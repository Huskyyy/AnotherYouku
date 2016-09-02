package com.huskyyy.anotheryouku.activity.base.viewholder;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.huskyyy.anotheryouku.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/30.
 */
public class LoadingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progressbar)
    public ProgressWheel progressBar;
    public LoadingViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
