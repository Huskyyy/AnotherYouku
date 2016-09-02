package com.huskyyy.anotheryouku.activity.base.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.huskyyy.anotheryouku.util.LogUtils;

/**
 * 针对LinearLayoutManager
 * 用于实现基础的逻辑，包括：
 * 1.初始加载时显示加载界面（可选）
 * 2.加载完成后若成功且有数据则显示加载结果（用户自定义）
 * 3.加载完成后若成功且无数据则显示无数据界面
 * 4.加载失败则显示加载失败界面
 * 5.若加载数据达到总数则显示无更多数据底部栏（可选）
 * 6.上拉加载更多，显示加载更多界面 (可选)
 * 7.若加载更多成功且有数据则直接显示
 * 8.若加载更多成功且无数据则显示无更多数据底部栏
 * 9.若加载更多失败显示加载更多失败底部栏
 * Created by Wang on 2016/8/24.
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_LOADING = 1;
    private static final int TYPE_NO_DATA = 2;
    private static final int TYPE_LOAD_FAILED = 3;
    private static final int TYPE_NO_MORE_DATA = 4;
    private static final int TYPE_LOAD_MORE = 5;
    private static final int TYPE_LOAD_MORE_FAILED = 6;
    // 子类的type偏置，注意子类只能选择大于0的值
    // 若子类从ViewHolder获取type信息，需要减去该值才能得到子类中所设置的type值
    protected static final int TYPE_OFFSET = 6;

    // 用于让子类设置是否显示初始加载动画
    private boolean enableShowLoading;
    // 用于让子类设置是否显示无更多数据底部栏
    private boolean enableShowNoMoreData;
    // 用于让子类设置是否上拉加载更多
    private boolean enableShowLoadMore;
    // 标志是否在加载更多
    private boolean loadingMore;

    // 用于表示数据是否加载完成
    private boolean dataLoaded;
    // 用与表示数据加载成功与否
    private boolean dataLoadSucceed;
    // 用于表示更多数据加载成功与否
    private boolean moreDataLoadSucceed;

    private int currentBottomHolderType;

    /**
     * 以下三个方法应在初始化时设置，不可在初始化后更改
     * @param b
     */
    protected void setShowLoading(boolean b) {
        this.enableShowLoading = b;
    }

    protected void setShowLoadMore(boolean b) {
        this.enableShowLoadMore = b;
    }

    protected void setShowNoMoreData(boolean b) {
        this.enableShowNoMoreData = b;
    }


    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public boolean isDataLoadSucceed() {
        return dataLoadSucceed;
    }

    public boolean isMoreDataLoadSucceed() {
        return moreDataLoadSucceed;
    }

    public boolean isLoadingMore() {
        return loadingMore;
    }

    /**
     * 以下三个方法用于在子类发生数据加载时，调用响应的notifyXXX方法之前设置
     * @param dataLoaded
     */
    protected void setDataLoaded(boolean dataLoaded) {
        this.dataLoaded = dataLoaded;
    }

    protected void setDataLoadSucceed(boolean dataLoadSucceed) {
        this.dataLoadSucceed = dataLoadSucceed;
    }

    protected void setMoreDataLoadSucceed(boolean moreDataLoadSucceed) {
        this.moreDataLoadSucceed = moreDataLoadSucceed;
    }

    /**
     * 重置所有设置。
     */
    public void reset() {
        dataLoaded = false;
        dataLoadSucceed = false;
        moreDataLoadSucceed = true;
        currentBottomHolderType = -1;
    }

    /**
     * 准备加载所有数据前调用，刷新加载界面
     * @param showLoading 是否强制清空数据并显示加载界面（实际子类加载完成后需要替换原有数据）
     */
    public void prepareLoad(boolean showLoading) {

        if(!showLoading && getBaseItemCount() == 0 || showLoading) {
            reset();
            notifyDataSetChanged();
        }
    }

    /**
     * 准备加载更多数据前调用，刷新底部栏
     */
    public void prepareLoadMore() {
        if(enableShowLoadMore) {
            moreDataLoadSucceed = true;
            loadingMore = true;
            notifyItemRemoved(getBaseItemCount());
            notifyItemInserted(getBaseItemCount());
        }
    }

    /**
     * 加载更多数据后调用
     */
    public void finishLoadMore() {
        loadingMore = false;
    }

    /**
     * 子类应在每次Item增减之后，调用该方法，以便及时更新底部栏。
     * 需要注意的是，当子类的Item数量从0到1或者从1到0时，即用户主动的添加、删除操作后，
     * 这时对应的界面数据是完全改变，应直接调用notifyDataSetChanged()更新。
     */
    protected void notifyBottomHolderChanged() {

        if(!enableShowLoadMore && !enableShowNoMoreData) {
            // 啥也不干
        }
        if(!enableShowLoadMore && enableShowNoMoreData) {
            // 加载了所有数据，且无数据底部栏还未显示
            if(dataLoaded && getBaseItemCount() > 0 && getBaseItemCount() == getBaseItemTotalCount()
                    && currentBottomHolderType == -1) {
                notifyItemInserted(getBaseItemCount());
            }
        }

        if(enableShowLoadMore && !enableShowNoMoreData) {
            // 加载了部分数据
            if(dataLoaded && getBaseItemCount() > 0
                    && getBaseItemCount() < getBaseItemTotalCount()) {
                // 加载更多失败，更新底部栏
                if(!moreDataLoadSucceed && currentBottomHolderType == TYPE_LOAD_MORE) {
                    notifyItemRemoved(getBaseItemCount());
                    notifyItemInserted(getBaseItemCount());
                }
                // 加载更多成功，更新
                if(moreDataLoadSucceed && currentBottomHolderType == TYPE_LOAD_MORE_FAILED) {
                    notifyItemRemoved(getBaseItemCount());
                    notifyItemInserted(getBaseItemCount());
                }
            }
            // 加载了所有数据
            if(dataLoaded && getBaseItemCount() > 0
                    && getBaseItemCount() == getBaseItemTotalCount()) {
                // 如果之前显示了底部栏，则移除
                if(currentBottomHolderType != -1) {
                    notifyItemRemoved(getBaseItemCount());
                    currentBottomHolderType = -1;
                }
            }
        }

        if(enableShowLoadMore && enableShowNoMoreData) {
            // 加载了部分数据
            if(dataLoaded && getBaseItemCount() > 0
                    && getBaseItemCount() < getBaseItemTotalCount()) {
                // 加载更多失败，更新底部栏
                if(!moreDataLoadSucceed
                        && (currentBottomHolderType == TYPE_LOAD_MORE
                            || currentBottomHolderType == TYPE_NO_MORE_DATA)) {
                    notifyItemRemoved(getBaseItemCount());
                    notifyItemInserted(getBaseItemCount());
                }
                // 加载更多成功，更新
                if(moreDataLoadSucceed
                        && (currentBottomHolderType == TYPE_LOAD_MORE_FAILED
                        || currentBottomHolderType == TYPE_NO_MORE_DATA)) {
                    notifyItemRemoved(getBaseItemCount());
                    notifyItemInserted(getBaseItemCount());
                }
            }
            // 加载了所有数据
            if(dataLoaded && getBaseItemCount() > 0
                    && getBaseItemCount() == getBaseItemTotalCount()) {
                // 如果之前的最后一个Item不为TYPE_NO_MORE_DATA，则转换
                if(currentBottomHolderType == TYPE_LOAD_MORE
                        || currentBottomHolderType == TYPE_LOAD_MORE_FAILED) {
                    notifyItemRemoved(getBaseItemCount());
                    notifyItemInserted(getBaseItemCount());
                }
            }
        }


    }


    /**
     * 子类不应该重写该方法
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADING:
                return getLoadingViewHolder(parent);
            case TYPE_NO_DATA:
                return getNoDataViewHolder(parent);
            case TYPE_LOAD_FAILED:
                return getLoadFailedViewHolder(parent);
            case TYPE_NO_MORE_DATA:
                return getNoMoreDataViewHolder(parent);
            case TYPE_LOAD_MORE:
                return getLoadMoreViewHolder(parent);
            case TYPE_LOAD_MORE_FAILED:
                return getLoadMoreFailedViewHolder(parent);
            }
        // 这里减去TYPE_OFFSET是为了抵消getItemViewType(int position)中设置的TYPE_OFFSET
        return onCreateBaseViewHolder(parent, viewType - TYPE_OFFSET);
    }

    /**
     * 子类实现该方法来生成ViewHolder
     * @param parent
     * @param viewType 实际的viewType减去TYPE_OFFSET，和用户传入的相同
     * @return
     */
    protected abstract RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType);

    /**
     * 子类不应该重写该方法
     * @param holder
     * @param position
     */
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = holder.getItemViewType();

        // 这里需要注意一下最后一个item到底是哪一种类型,
        // 如果没有及时调用notifyBottomHolderChanged()更新该ViewHolder，则不能绑定数据
        if(getBaseItemCount() != 0 && position == getBaseItemCount()) {
            currentBottomHolderType = type;
            if(currentBottomHolderType != getItemViewType(position)) {
                return;
            }
        }

        // 数据未加载且需要显示加载界面
        if(!dataLoaded && enableShowLoading && type == TYPE_LOADING) {
            onBindLoadingViewHolder(holder);
            return;
        }
        // 数据加载了但没有数据
        if(dataLoaded && dataLoadSucceed && getBaseItemCount() == 0 && type == TYPE_NO_DATA) {
            onBindNoDataViewHolder(holder);
            return;
        }
        // 数据加载失败
        if(dataLoaded && !dataLoadSucceed && type == TYPE_LOAD_FAILED) {
            onBindLoadFailedViewHolder(holder);
            return;
        }
        // 数据加载了，没有更多数据，且需要显示无更多数据底部栏
        if(position == getBaseItemTotalCount() && dataLoaded && getBaseItemCount() > 0
                && enableShowNoMoreData && type == TYPE_NO_MORE_DATA) {
            onBindNoMoreDataViewHolder(holder);
            return;
        }
        // 数据加载了，有更多数据可以加载，且需要上拉加载更多,且还没加载
        if(position == getBaseItemCount() && dataLoaded && getBaseItemCount() > 0
                && getBaseItemCount() < getBaseItemTotalCount() && enableShowLoadMore
                && moreDataLoadSucceed && type == TYPE_LOAD_MORE) {
            onBindLoadMoreViewHolder(holder);
            return;
        }
        // 数据加载了，有更多数据可以加载，且需要上拉加载更多,且尝试过加载更多，但是加载更多失败了
        if(position == getBaseItemCount() && dataLoaded && getBaseItemCount() > 0
                && getBaseItemCount() < getBaseItemTotalCount() && enableShowLoadMore
                && !moreDataLoadSucceed && type == TYPE_LOAD_MORE_FAILED) {
            onBindLoadMoreFailedViewHolder(holder);
            return;
        }

        // 其余情况交给子类处理
        onBindBaseViewHolder(holder, position);
    }

    /**
     * 子类实现该方法来绑定数据
     * @param holder
     * @param position
     */
    protected abstract void onBindBaseViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * 子类不应该重写该方法.
     * 对于用户设置的viewType,一律加上TYPE_OFFSET,
     * 因此需要让用户设置的viewType一律大于0,否则抛出异常.
     * @param position
     * @return
     */
    @Override
    public final int getItemViewType(int position) {
        // 数据未加载且需要显示加载界面
        if(!dataLoaded && enableShowLoading) {
            return TYPE_LOADING;
        }
        // 数据加载了但没有数据
        if(dataLoaded && dataLoadSucceed && getBaseItemCount() == 0) {
            return TYPE_NO_DATA;
        }
        // 数据加载失败
        if(dataLoaded && !dataLoadSucceed) {
            return TYPE_LOAD_FAILED;
        }
        // 数据加载了，没有更多数据，且需要显示无更多数据底部栏
        if(position == getBaseItemTotalCount() && dataLoaded && getBaseItemCount() > 0
                && enableShowNoMoreData) {
            return TYPE_NO_MORE_DATA;
        }
        // 数据加载了，有更多数据可以加载，且需要上拉加载更多,且还没加载
        if(position == getBaseItemCount() && dataLoaded && getBaseItemCount() > 0
                && getBaseItemCount() < getBaseItemTotalCount() && enableShowLoadMore
                && moreDataLoadSucceed) {
            return TYPE_LOAD_MORE;
        }
        // 数据加载了，有更多数据可以加载，且需要上拉加载更多,且尝试过加载更多，但是加载更多失败了
        if(position == getBaseItemCount() && dataLoaded && getBaseItemCount() > 0
                && getBaseItemCount() < getBaseItemTotalCount() && enableShowLoadMore
                && !moreDataLoadSucceed) {
            return TYPE_LOAD_MORE_FAILED;
        }
        // 其余情况交给子类处理
        int type = getBaseItemViewType(position);
        if(type <= 0) {
            throw new RuntimeException("Must return a positive integer");
        }
        return getBaseItemViewType(position) + TYPE_OFFSET;
    }

    /**
     * 子类实现该方法来设置对应的类型
     * @param position
     * @return 原始的viewType
     */
    protected abstract int getBaseItemViewType(int position);

    /**
     * 子类不应该重写该方法
     * @return
     */
    @Override
    public final int getItemCount() {
        // 数据未加载且需要显示加载界面
        if(!dataLoaded && enableShowLoading) {
            return 1;
        }
        // 数据加载了但没有数据
        if(dataLoaded && dataLoadSucceed && getBaseItemCount() == 0) {
            return 1;
        }
        // 数据加载失败
        if(dataLoaded && !dataLoadSucceed) {
            return 1;
        }
        // 数据加载了，没有更多数据，且需要显示无更多数据底部栏
        if(dataLoaded && getBaseItemCount() > 0 && getBaseItemCount() == getBaseItemTotalCount()
                && enableShowNoMoreData) {
            return getBaseItemCount() + 1;
        }
        // 数据加载了，有更多数据可以加载，且需要上拉加载更多,且还没加载
        if(dataLoaded && getBaseItemCount() > 0 && getBaseItemCount() < getBaseItemTotalCount()
                && enableShowLoadMore && moreDataLoadSucceed) {
            return getBaseItemCount() + 1;
        }
        // 数据加载了，有更多数据可以加载，且需要上拉加载更多,且尝试过加载更多，但是加载更多失败了
        if(dataLoaded && getBaseItemCount() > 0 && getBaseItemCount() < getBaseItemTotalCount()
                && enableShowLoadMore && !moreDataLoadSucceed) {
            return getBaseItemCount() + 1;
        }
        // 其余情况交给子类处理
        return getBaseItemCount();
    }

    /**
     * 子类实现该方法设置目前的Item数量
     * @return
     */
    protected abstract int getBaseItemCount();

    /**
     * 子类实现该方法设置可以加载的Item总数
     * @return
     */
    protected abstract int getBaseItemTotalCount();


    /**
     * 让子类返回需要的加载界面，用于onCreateViewHolder
      */
    protected abstract RecyclerView.ViewHolder getLoadingViewHolder(ViewGroup parent);
    /**
     * 无数据界面
     */
    protected abstract RecyclerView.ViewHolder getNoDataViewHolder(ViewGroup parent);
    /**
     * 加载失败界面
     */
    protected abstract RecyclerView.ViewHolder getLoadFailedViewHolder(ViewGroup parent);
    /**
     * 无数据底部栏
     */
    protected abstract RecyclerView.ViewHolder getNoMoreDataViewHolder(ViewGroup parent);
    /**
     * 加载更多底部栏
      */
    protected abstract RecyclerView.ViewHolder getLoadMoreViewHolder(ViewGroup parent);
    /**
     * 加载更多失败底部栏
      */
    protected abstract RecyclerView.ViewHolder getLoadMoreFailedViewHolder(ViewGroup parent);


    /**
     * 绑定加载界面，用于onBindViewHolder
      */
    protected abstract void onBindLoadingViewHolder(RecyclerView.ViewHolder viewHolder);
    /**
     * 绑定无数据界面
      */
    protected abstract void onBindNoDataViewHolder(RecyclerView.ViewHolder viewHolder);
    /**
     * 绑定加载失败界面
     */
    protected abstract void onBindLoadFailedViewHolder(RecyclerView.ViewHolder viewHolder);
    /**
     * 绑定无数据底部栏
     */
    protected abstract void onBindNoMoreDataViewHolder(RecyclerView.ViewHolder viewHolder);
    /**
     * 绑定加载更多底部栏
     */
    protected abstract void onBindLoadMoreViewHolder(RecyclerView.ViewHolder viewHolder);
    /**
     * 绑定加载更多失败底部栏
     */
    protected abstract void onBindLoadMoreFailedViewHolder(RecyclerView.ViewHolder viewHolder);

}
