package com.huskyyy.anotheryouku.activity.main.home.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.util.ImageLoader;

import java.util.List;

/**
 * Created by Wang on 2016/7/29.
 */
public class BannerAdapter extends PagerAdapter {

    // list.size() should be smaller than FAKE_SIZE
    public static final int FAKE_SIZE = 20;

    private Context context;
    private ViewPager banner;
    private List<VideoData> list;
    private BannerClickListener bannerClickListener;

    public BannerAdapter(Context context, ViewPager banner, List<VideoData> list) {
        this.context = context;
        this.banner = banner;
        this.list = list;
    }

    public void replaceData(List<VideoData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<VideoData> getData() {
        return list;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return FAKE_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int realPosition = position % list.size();
        View root = LayoutInflater.from(context)
                .inflate(R.layout.view_banner_content, container, false);
        ImageView posterImageView = (ImageView) root.findViewById(R.id.iv_poster);
        TextView titleTextView = (TextView) root.findViewById(R.id.tv_title);
        ImageLoader.loadNormalImage(list.get(realPosition).getBigThumbnail(), posterImageView);
        titleTextView.setText(list.get(realPosition).getTitle());

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bannerClickListener != null)
                    bannerClickListener.onClick(realPosition);
            }
        });
        container.addView(root);
        return root;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = banner.getCurrentItem();
        if (position == 0) {
            position = list.size();
            banner.setCurrentItem(position, false);
        } else if (position == FAKE_SIZE - 1) {
            position = list.size() - 1;
            banner.setCurrentItem(position, false);
        }
    }

    public interface BannerClickListener {
        void onClick(int position);
    }

    public void setBannerClickListener(BannerClickListener bannerClickListener) {
        this.bannerClickListener = bannerClickListener;
    }
}
