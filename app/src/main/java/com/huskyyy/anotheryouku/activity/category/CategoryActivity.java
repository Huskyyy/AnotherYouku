package com.huskyyy.anotheryouku.activity.category;

import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.base.activity.BaseActivity;
import com.huskyyy.anotheryouku.activity.base.activity.ToolbarBaseActivity;
import com.huskyyy.anotheryouku.activity.base.adapter.BaseFragmentPagerAdapter;
import com.huskyyy.anotheryouku.activity.category.genre.CategoryGenreFragment;
import com.huskyyy.anotheryouku.activity.category.genre.CategoryGenrePresenter;
import com.huskyyy.anotheryouku.activity.category.home.CategoryHomeFragment;
import com.huskyyy.anotheryouku.activity.category.home.CategoryHomePresenter;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.data.base.DataConstants;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wang on 2016/8/22.
 */
public class CategoryActivity extends ToolbarBaseActivity {

    public static final String CATEGORY = "com.huskyyy.anotheryouku.activity.category.CATEGORY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        category = getIntent().getStringExtra(CATEGORY);
        setTitle(category);
        setupViewPager();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {

        final BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());

        CategoryHomeFragment homeFragment = new CategoryHomeFragment();
        final CategoryHomePresenter presenter = new CategoryHomePresenter(DataSource.getInstance(),
                homeFragment, category);
        adapter.addFragment(homeFragment, getString(R.string.fragment_home_title));

        if(DataConstants.videoCategoryMap.isEmpty()) {
            DataConstants.generateCategoryMap(this);
        }

        if(DataConstants.videoCategoryMap.get(category) != null) {
            for(String genre : DataConstants.videoCategoryMap.get(category)) {
                CategoryGenreFragment fragment = new CategoryGenreFragment();
                CategoryGenrePresenter presenter1 = new CategoryGenrePresenter(this,
                        DataSource.getInstance(), fragment, category, genre);
                adapter.addFragment(fragment, genre);
            }
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state != ViewPager.SCROLL_STATE_IDLE) {
                    ImageLoader.pauseImageLoading();
                } else {
                    ImageLoader.resumeImageLoading();
                }
            }
        });
    }
}
