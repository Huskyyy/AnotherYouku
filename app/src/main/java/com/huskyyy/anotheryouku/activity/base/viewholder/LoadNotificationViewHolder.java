package com.huskyyy.anotheryouku.activity.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/27.
 */
public class LoadNotificationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_load_notification)
    public TextView notificationTextView;
    @BindView(R.id.tv_load)
    public TextView loadTextView;
    public LoadNotificationViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
