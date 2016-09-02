package com.huskyyy.anotheryouku.activity.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huskyyy.anotheryouku.R;

import org.apmem.tools.layouts.FlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/27.
 */
public class TagsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.flow_tags)
    public FlowLayout tagsFlow;
    public TagsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
