package com.huskyyy.anotheryouku.activity.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Wang on 2016/8/26.
 */
public class SimpleRecyclerViewAdapter extends BaseRecyclerViewAdapter {

    public SimpleRecyclerViewAdapter() {
        reset();
    }

    @Override
    protected int getBaseItemCount() {
        return 0;
    }

    @Override
    protected int getBaseItemTotalCount() {
        return 0;
    }

    @Override
    protected int getBaseItemViewType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getLoadFailedViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getLoadingViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getLoadMoreFailedViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getLoadMoreViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getNoDataViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getNoMoreDataViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected void onBindBaseViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindLoadFailedViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected void onBindLoadingViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected void onBindLoadMoreFailedViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected void onBindLoadMoreViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected void onBindNoDataViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected void onBindNoMoreDataViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
