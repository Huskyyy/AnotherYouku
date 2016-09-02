package com.huskyyy.anotheryouku.activity.main.home.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.adapter.SimpleRecyclerViewAdapter;
import com.huskyyy.anotheryouku.activity.base.viewholder.CategoryFooterViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.CategoryHeaderViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.HotFooterViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.HotHeaderViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.LoadNotificationViewHolder;
import com.huskyyy.anotheryouku.activity.base.viewholder.TwoVideosViewHolder;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.data.base.VideosByCategory;
import com.huskyyy.anotheryouku.widget.VideoCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/26.
 */
public class HomeListAdapter extends SimpleRecyclerViewAdapter {

    private static final int TYPE_BANNER = 1;
    private static final int TYPE_HOT_HEADER = 2;
    private static final int TYPE_CATEGORY_HEADER = 3;
    private static final int TYPE_VIDEOS = 4;
    private static final int TYPE_HOT_FOOTER = 5;
    private static final int TYPE_CATEGORY_FOOTER = 6;

    private static final int SCROLL_INTERVAL = 5000;

    private static final int MSG_SCROLL_BANNER = 1;

    private Context context;
    private List<VideosByCategory> data;

    private int bannerNum;
    private int bannerPosition;
    private boolean bannerTouched;
    private BannerViewHolder bannerViewHolder;
    private HomeItemClickListener homeItemClickListener;

    private boolean[] dataRefreshed;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SCROLL_BANNER:
                    if(bannerViewHolder != null) {
                        if (!bannerTouched) {
                            bannerPosition = (bannerPosition + 1) % BannerAdapter.FAKE_SIZE;
                            if (bannerPosition == BannerAdapter.FAKE_SIZE - 1) {
                                bannerViewHolder.banner.setCurrentItem(bannerNum - 1, false);
                            } else {
                                bannerViewHolder.banner.setCurrentItem(bannerPosition);
                            }
                            sendEmptyMessageDelayed(MSG_SCROLL_BANNER, SCROLL_INTERVAL);
                        }
                    }
            }
        }
    };

    public void enableBannerScroll(boolean enable) {
        if(enable) {
            if(!handler.hasMessages(MSG_SCROLL_BANNER)) {
                handler.sendEmptyMessageDelayed(MSG_SCROLL_BANNER, SCROLL_INTERVAL);
            }
        } else {
            handler.removeMessages(MSG_SCROLL_BANNER);
        }
    }

    public HomeListAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        reset();
    }

    /**
     * 替换adapter的数据，若loadSucceed为false，说明list为空，加载失败
     * @param list
     * @param loadSucceed
     */
    public void replaceAllData(List<VideosByCategory> list, boolean loadSucceed) {

        if (loadSucceed) {
            data = list;
            dataRefreshed = new boolean[data.size()];
            Arrays.fill(dataRefreshed, true);
            setDataLoadSucceed(true);
            setDataLoaded(true);
            notifyDataSetChanged();
        } else if (getBaseItemCount() == 0) {
            setDataLoaded(true);
            notifyDataSetChanged();
        }


    }

    public void replaceCategoryData(int position, VideosByCategory videos, boolean refreshed) {
        dataRefreshed[position + 1] = true;
        if(refreshed) {
            data.set(position + 1, videos);
            notifyItemChanged(position * 4 + 2);
            notifyItemChanged(position * 4 + 3);
            notifyItemChanged(position * 4 + 4);
        } else {
            notifyItemChanged(position * 4 + 4);
        }

    }

    @Override
    protected int getBaseItemCount() {
        if(data.size() == 0) {
            return 0;
        } else{
            return (data.size() - 1) * 4 + 1;
        }
    }

    @Override
    protected int getBaseItemTotalCount() {
        return getBaseItemCount();
    }

    @Override
    protected int getBaseItemViewType(int position) {
        if(position == 0)
            return TYPE_BANNER;

        if(position == 1)
            return TYPE_HOT_HEADER;

        if(position == 4)
            return TYPE_HOT_FOOTER;

        if((position - 1) % 4 == 0)
            return TYPE_CATEGORY_HEADER;

        if((position - 1) % 4 == 3)
            return TYPE_CATEGORY_FOOTER;

        return TYPE_VIDEOS;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_BANNER:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_banner, parent, false);
                BannerViewHolder holder =  new BannerViewHolder(view);
                initBannerClick(holder);
                bannerViewHolder = holder;
                return holder;
            case TYPE_HOT_HEADER:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_hot_header, parent, false);
                HotHeaderViewHolder holder1 = new HotHeaderViewHolder(view);
                initHotHeader(holder1);
                return holder1;
            case TYPE_HOT_FOOTER:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_hot_footer, parent, false);
                HotFooterViewHolder holder2 = new HotFooterViewHolder(view);
                initHotFooterClick(holder2);
                return holder2;
            case TYPE_CATEGORY_HEADER:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_category_header, parent, false);
                CategoryHeaderViewHolder holder3 = new CategoryHeaderViewHolder(view);
                initCateHeaderClick(holder3);
                return holder3;
            case TYPE_CATEGORY_FOOTER:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_category_footer, parent, false);
                CategoryFooterViewHolder holder4 = new CategoryFooterViewHolder(view);
                initCateFooterClick(holder4);
                return holder4;
            case TYPE_VIDEOS:
            default:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_2videos, parent, false);
                TwoVideosViewHolder holder5 = new TwoVideosViewHolder(view);
                initVideosClick(holder5);
                return holder5;

        }
    }

    @Override
    protected void onBindBaseViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            BannerViewHolder viewHolder =  (BannerViewHolder) holder;
            initBanner(viewHolder);
        } else if(position == 1) {

        } else if(position == 4) {
            HotFooterViewHolder viewHolder = (HotFooterViewHolder) holder;
            initHotFooter(viewHolder);
        } else if(position % 4 == 1) {
            CategoryHeaderViewHolder viewHolder = (CategoryHeaderViewHolder) holder;
            initCateHeader(position, viewHolder);
        } else if(position % 4 == 0) {
            CategoryFooterViewHolder viewHolder = (CategoryFooterViewHolder) holder;
            initCateFooter(position, viewHolder);
        } else {
            TwoVideosViewHolder viewHolder = (TwoVideosViewHolder) holder;
            initVideos(position, viewHolder);
        }
    }

    @Override
    protected void onBindNoDataViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected RecyclerView.ViewHolder getNoDataViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_load_notification, parent, false);
        LoadNotificationViewHolder holder = new LoadNotificationViewHolder(view);
        initNoData(holder);
        return holder;
    }

    @Override
    protected void onBindLoadFailedViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected RecyclerView.ViewHolder getLoadFailedViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_load_notification, parent, false);
        LoadNotificationViewHolder holder = new LoadNotificationViewHolder(view);
        initLoadFailed(holder);
        return holder;
    }


    private void initBannerClick(BannerViewHolder holder) {
        VideosByCategory item = data.get(0);
        bannerNum = item.getVideos().length;

        // 由于加载数目不变，因此直接设置indicator
        final ImageView indicators[] = new ImageView[bannerNum];
        for(int i = 0; i < bannerNum; i++) {
            ImageView imageView = (ImageView) LayoutInflater.from(context)
                    .inflate(R.layout.view_banner_indicator, holder.indicator, false);
            holder.indicator.addView(imageView);
            indicators[i] = imageView;
        }

        final BannerAdapter bannerAdapter = new BannerAdapter(context, holder.banner,
                Arrays.asList(item.getVideos()));
        bannerAdapter.setBannerClickListener(new BannerAdapter.BannerClickListener() {
            @Override
            public void onClick(int pos) {
                if(homeItemClickListener != null) {
                    homeItemClickListener.onBannerClick(bannerAdapter.getData().get(pos));
                }
            }
        });
        holder.banner.setAdapter(bannerAdapter);
        holder.banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int pos) {
                bannerPosition = pos;
                pos %= bannerNum;
                for(int i = 0; i < bannerNum; i++) {
                    if(i == pos) {
                        indicators[i].setImageResource(R.drawable.ic_pager_indicator_selected_18dp);
                    } else {
                        indicators[i].setImageResource(R.drawable.ic_pager_indicator_18dp);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        holder.banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN
                        || action == MotionEvent.ACTION_MOVE) {
                    handler.removeMessages(MSG_SCROLL_BANNER);
                } else if (action == MotionEvent.ACTION_UP) {
                    bannerTouched = false;
                    handler.sendEmptyMessageDelayed(MSG_SCROLL_BANNER, SCROLL_INTERVAL);
                }
                return false;
            }
        });
        holder.banner.requestDisallowInterceptTouchEvent(true);
    }

    private void initBanner(BannerViewHolder holder) {
        VideosByCategory item = data.get(0);
        final List<VideoData> bannerData = Arrays.asList(item.getVideos());
        ((BannerAdapter)holder.banner.getAdapter()).replaceData(bannerData);
        holder.banner.setCurrentItem(bannerPosition % bannerNum);
        enableBannerScroll(false);
        enableBannerScroll(true);
    }

    private void initHotHeader(HotHeaderViewHolder viewHolder){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeItemClickListener != null) {
                    homeItemClickListener.onHotRankClick();
                }
            }
        };
        viewHolder.itemView.setOnClickListener(listener);
        viewHolder.exploreTextView.setOnClickListener(listener);
    }

    private void initHotFooterClick(final HotFooterViewHolder viewHolder) {
        final ObjectAnimator animator = ObjectAnimator
                .ofFloat(viewHolder.refreshImageView, "rotation", 0f, 360f);
        animator.setDuration(500);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(null);
        viewHolder.refreshImageView.setTag(animator);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeItemClickListener != null && dataRefreshed[1]) {
                    dataRefreshed[1] = false;
                    viewHolder.refreshTextView.setText(
                            context.getString(R.string.card_refreshing));
                    animator.start();
                    homeItemClickListener.onCategoryRefreshClick(0,
                            data.get(1).getCategory());
                }
            }
        };
        viewHolder.refreshTextView.setOnClickListener(listener);
        viewHolder.refreshImageView.setOnClickListener(listener);
    }

    private void initHotFooter(HotFooterViewHolder viewHolder) {
        if(dataRefreshed[1]) {
            viewHolder.refreshTextView.setText(context.getString(R.string.card_refresh));
            ((ObjectAnimator)viewHolder.refreshImageView.getTag()).cancel();
            viewHolder.refreshImageView.setRotation(0f);
        } else {
            viewHolder.refreshTextView.setText(context.getString(R.string.card_refreshing));
            ((ObjectAnimator)viewHolder.refreshImageView.getTag()).cancel();
            ((ObjectAnimator)viewHolder.refreshImageView.getTag()).start();
        }
    }

    private void initCateHeaderClick(final CategoryHeaderViewHolder viewHolder) {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeItemClickListener != null) {
                    homeItemClickListener.onCategoryClick(
                            data.get((viewHolder.getAdapterPosition() - 1) / 4 + 1).getCategory());
                }
            }
        };
        viewHolder.itemView.setOnClickListener(listener);
        viewHolder.exploreTextView.setOnClickListener(listener);
    }

    private void initCateHeader(int position, CategoryHeaderViewHolder viewHolder) {
        final VideosByCategory item = data.get((position - 1) / 4 + 1);
        viewHolder.logoImageView.setImageResource(item.getLogoId());
        viewHolder.nameTextView.setText(item.getCategory());
    }

    private void initCateFooterClick(final CategoryFooterViewHolder viewHolder) {
        final ObjectAnimator animator = ObjectAnimator
                .ofFloat(viewHolder.refreshImageView, "rotation", 0f, 360f);
        animator.setDuration(500);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(null);
        viewHolder.refreshImageView.setTag(animator);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dataPos = viewHolder.getAdapterPosition() / 4;
                if(homeItemClickListener != null && dataRefreshed[dataPos]) {
                    dataRefreshed[dataPos] = false;
                    viewHolder.refreshTextView.setText("");
                    animator.start();
                    homeItemClickListener.onCategoryRefreshClick(dataPos - 1,
                            data.get(dataPos).getCategory());
                }
            }
        };
        viewHolder.refreshTextView.setOnClickListener(listener);
        viewHolder.refreshImageView.setOnClickListener(listener);
        viewHolder.showMoreCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeItemClickListener != null) {
                    int dataPos = viewHolder.getAdapterPosition() / 4;
                    homeItemClickListener.onCategoryClick(data.get(dataPos).getCategory());
                }
            }
        });
    }

    private void initCateFooter(int position,
                                final CategoryFooterViewHolder viewHolder) {
        VideosByCategory item = data.get((position - 1) / 4 + 1);
        final int dataPos = position / 4;
        if(dataRefreshed[dataPos]) {
            viewHolder.refreshTextView.setText(context.getString(R.string.card_refresh));
            ((ObjectAnimator)viewHolder.refreshImageView.getTag()).cancel();
            viewHolder.refreshImageView.setRotation(0f);
        } else {
            viewHolder.refreshTextView.setText("");
            ((ObjectAnimator)viewHolder.refreshImageView.getTag()).cancel();
            ((ObjectAnimator)viewHolder.refreshImageView.getTag()).start();
        }

        viewHolder.showMoreTextView.setText(context.getString(R.string.card_show_more)
                + item.getCategory());
    }

    private void initVideosClick(final TwoVideosViewHolder viewHolder) {

        viewHolder.videoCardView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    VideosByCategory item = data.get((position - 1) / 4 + 1);
                    VideoData data0;
                    if(position % 4 == 2) {
                        data0 = item.getVideos()[0];
                    } else {
                        data0 = item.getVideos()[2];
                    }
                    homeItemClickListener.onVideoClick(data0);
                }
            }
        });

        viewHolder.videoCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    VideosByCategory item = data.get((position - 1) / 4 + 1);
                    final VideoData data1;
                    if(position % 4 == 2) {
                        data1 = item.getVideos()[1];
                    } else {
                        data1 = item.getVideos()[3];
                    }
                    homeItemClickListener.onVideoClick(data1);
                }
            }
        });
    }

    private void initVideos(int position, TwoVideosViewHolder viewHolder) {
        VideosByCategory item = data.get((position - 1) / 4 + 1);
        final VideoData data0;
        final VideoData data1;
        if(position % 4 == 2) {
            data0 = item.getVideos()[0];
            data1 = item.getVideos()[1];
        } else {
            data0 = item.getVideos()[2];
            data1 = item.getVideos()[3];
        }

        viewHolder.videoCardView0.setVideo(data0);
        viewHolder.videoCardView1.setVideo(data1);
    }

    private void initLoadFailed(LoadNotificationViewHolder viewHolder) {
        viewHolder.notificationTextView.setText(R.string.load_failed);
        viewHolder.loadTextView.setText(R.string.reload);
        viewHolder.loadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeItemClickListener != null) {
                    homeItemClickListener.onLoadClick();
                }
            }
        });
    }

    private void initNoData(LoadNotificationViewHolder viewHolder) {
        viewHolder.notificationTextView.setText(R.string.no_video_data);
        viewHolder.loadTextView.setText(R.string.refresh);
        viewHolder.loadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeItemClickListener != null) {
                    homeItemClickListener.onLoadClick();
                }
            }
        });
    }




    public interface HomeItemClickListener {

        void onLoadClick();

        void onBannerClick(VideoData data);

        void onHotRankClick();

        void onCategoryClick(String category);

        void onCategoryRefreshClick(int position, String category);

        void onVideoClick(VideoData data);
    }

    public void setHomeItemClickListener(HomeItemClickListener listener) {
        homeItemClickListener = listener;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.banner_poster)
        ViewPager banner;
        @BindView(R.id.indicator)
        LinearLayout indicator;
        public BannerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
