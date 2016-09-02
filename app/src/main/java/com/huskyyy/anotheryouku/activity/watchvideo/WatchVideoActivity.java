package com.huskyyy.anotheryouku.activity.watchvideo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.adapter.BaseFragmentPagerAdapter;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.data.base.VideoData;
import com.huskyyy.anotheryouku.activity.watchvideo.comment.CommentFragment;
import com.huskyyy.anotheryouku.activity.watchvideo.comment.CommentPresenter;
import com.huskyyy.anotheryouku.activity.watchvideo.related.RelatedVideosFragment;
import com.huskyyy.anotheryouku.activity.watchvideo.related.RelatedVideosPresenter;
import com.huskyyy.anotheryouku.activity.watchvideo.summary.SummaryFragment;
import com.huskyyy.anotheryouku.activity.watchvideo.summary.SummaryPresenter;
import com.huskyyy.anotheryouku.util.KeyboardUtils;
import com.huskyyy.anotheryouku.util.LogUtils;
import com.huskyyy.anotheryouku.widget.SendCommentView;
import com.ykcloud.sdk.opentools.player.VODPlayerStatListener;
import com.youku.player.base.YoukuBasePlayerActivity;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;
import com.youku.player.plugin.MediaPlayerDelegate;
import com.youku.player.plugin.fullscreen.PluginFullScreenPlay;
import com.youku.player.plugin.small.PluginSmall;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WatchVideoActivity extends YoukuBasePlayerActivity implements VODPlayerStatListener {

    public static final String VIDEO_DATA = "com.huskyyy.anotheryouku.activity.watchvideo.VIDEODATA";
    public static final String USER_DATA = "com.huskyyy.anotheryouku.activity.watchvideo.USERDATA";

    @BindView(R.id.full_holder)
    YoukuPlayerView youkuPlayerView;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private VideoData videoData;
    private User user;
    private String videoId;
    private String userId;

    //视频跳过时长
    private int point = 0;// 播放进度
    private YoukuPlayer youkuPlayer;
    private MediaPlayerDelegate mediaPlayerDelegate;
    private PluginFullScreenPlay pluginFullScreenPlay;// 全屏插件
    private PluginSmall pluginSmall;// 小屏插件

    private CommentFragment commentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayerDelegate.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerDelegate.release();
    }

    /**
     * 点击后退隐藏表情键盘
      */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && commentFragment.hasFocus()) {
            commentFragment.clearFocus();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击输入框外的区域隐藏软键盘
      */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                KeyboardUtils.hideSoftInput(WatchVideoActivity.this);
                commentFragment.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据用户点击的坐标来判断是否隐藏键盘
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        SendCommentView sendCommentView = commentFragment.getSendCommentView();
        if(sendCommentView != null) {
            int[] l = { 0, 0 };
            sendCommentView.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + sendCommentView.getHeight(), right = left
                    + sendCommentView.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 忽略
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void init() {

        setupViewPager();

        //播放器初始化 初始化成功后回调onInitializationSuccess方法
        youkuPlayerView.initialize(this);

    }

    private void setupViewPager() {
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());
        SummaryFragment summaryFragment = new SummaryFragment();
        adapter.addFragment(summaryFragment, getString(R.string.fragment_summary_title));
        commentFragment = new CommentFragment();
        adapter.addFragment(commentFragment, getString(R.string.fragment_comment_title));
        RelatedVideosFragment relatedVideosFragment = new RelatedVideosFragment();
        adapter.addFragment(relatedVideosFragment, getString(R.string.fragment_related_title));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        tabs.setViewPager(viewPager);
        tabs.setBackgroundResource(R.color.white);
        tabs.setTextSize(40);
        final LinearLayout mTabsLinearLayout;
        mTabsLinearLayout = ((LinearLayout)tabs.getChildAt(0));
        for(int i=0; i < mTabsLinearLayout.getChildCount(); i++){
            TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
            if(i == 0){
                tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            } else {
                tv.setTextColor(Color.BLACK);
            }
        }
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                for(int i=0; i < mTabsLinearLayout.getChildCount(); i++){
                    TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
                    if(i == position){
                        tv.setTextColor(ContextCompat.getColor(
                                WatchVideoActivity.this, R.color.colorPrimary));
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        videoData = getIntent().getParcelableExtra(VIDEO_DATA);
        user = getIntent().getParcelableExtra(USER_DATA);
        videoId = videoData.getId();

        SummaryPresenter summaryPresenter = new SummaryPresenter(this, DataSource.getInstance(),
                summaryFragment, videoData, user);
        CommentPresenter commentPresenter = new CommentPresenter(this, DataSource.getInstance(),
                commentFragment, videoData);
        RelatedVideosPresenter relatedVideosPresenter = new RelatedVideosPresenter(
                DataSource.getInstance(), relatedVideosFragment, videoData);
    }

    //处理状态回调
    @Override
    public void onPlayerStat(int stat, int ext) {
        //ToastUtils.show("onPlayerStat : " + stat, 2000);
    }

    /**
     * 播放器初始化成功回调
     */
    @Override
    public void onInitializationSuccess(YoukuPlayer player) {
        super.onInitializationSuccess(player);
        this.youkuPlayer = player;
        mediaPlayerDelegate = getMediaPlayerDelegate();
        //设置状态回调函数
        mediaPlayerDelegate.setVodPlayerStatListener(this);
        // must!!!
        setPlayerController(youkuPlayer.getPlayerUiControl());
        //全屏插件
        pluginFullScreenPlay = new PluginFullScreenPlay(this, mediaPlayerDelegate);
        //半屏插件
        pluginSmall = new PluginSmall(this, mediaPlayerDelegate);
        setmPluginSmallScreenPlay(pluginSmall);
        setmPluginFullScreenPlay(pluginFullScreenPlay);
        addPlugins();

        play();
    }

    private void play() {
        //播放视频
        //youkuPlayer.playVideo(videoId, false, point);
        youkuPlayer.playVideo(videoId, false, point);
        //全屏播放（横屏）
        //mediaPlayerDelegate.goFullScreen();
        //半屏播放（竖屏）
        mediaPlayerDelegate.goSmall();
    }

    @Override
    public void setPadHorizontalLayout() {

    }

    @Override
    public void onFullscreenListener() {

    }

    @Override
    public void onSmallscreenListener() {

    }

}
