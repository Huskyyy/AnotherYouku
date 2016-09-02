package com.huskyyy.anotheryouku.activity.main;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.huskyyy.anotheryouku.R;
import com.huskyyy.anotheryouku.activity.account.AccountUtils;
import com.huskyyy.anotheryouku.activity.account.LoginActivity;
import com.huskyyy.anotheryouku.activity.base.activity.BaseActivity;
import com.huskyyy.anotheryouku.activity.base.activity.ToolbarBaseActivity;
import com.huskyyy.anotheryouku.activity.base.adapter.BaseFragmentPagerAdapter;
import com.huskyyy.anotheryouku.activity.main.grid.GridFragment;
import com.huskyyy.anotheryouku.activity.main.grid.GridPresenter;
import com.huskyyy.anotheryouku.api.ErrorConstants;
import com.huskyyy.anotheryouku.data.BaseCallback;
import com.huskyyy.anotheryouku.data.DataSource;
import com.huskyyy.anotheryouku.activity.main.home.HomeFragment;
import com.huskyyy.anotheryouku.activity.main.home.HomePresenter;
import com.huskyyy.anotheryouku.data.base.Error;
import com.huskyyy.anotheryouku.data.base.User;
import com.huskyyy.anotheryouku.util.ImageLoader;
import com.huskyyy.anotheryouku.util.NetUtils;
import com.huskyyy.anotheryouku.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;

public class MainActivity extends ToolbarBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_LOGIN = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private CircleImageView avatarImageView;
    private TextView accountNameTextView;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setupViewPager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupNavigationView();
    }

    private void setupViewPager() {
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());
        HomeFragment homeFragment = new HomeFragment();
        adapter.addFragment(homeFragment, getString(R.string.fragment_home_title));
        GridFragment gridFragment = new GridFragment();
        adapter.addFragment(gridFragment, getString(R.string.fragment_category_title));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        tabLayout.setupWithViewPager(viewPager);
        HomePresenter homePresenter = new HomePresenter(this, DataSource.getInstance(), homeFragment);
        GridPresenter gridPresenter = new GridPresenter(gridFragment);
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        avatarImageView = (CircleImageView) headerView.findViewById(R.id.iv_avatar);
        accountNameTextView = (TextView) headerView.findViewById(R.id.tv_account_name);
        loginTextView = (TextView) headerView.findViewById(R.id.tv_login);

        Account[] accounts = AccountUtils.getAccounts(this, LoginActivity.ACCOUNT_TYPE);
        if(accounts == null || accounts.length == 0) {
            setNavigationHeaderByUser(null);
        } else {
            setNavigationHeaderByAuth(1);
        }
    }

    private void setNavigationHeaderByAuth(final int count) {
        if(!NetUtils.isNetworkAvailable()) {
            ToastUtils.showShort(R.string.network_unavailable);
            return;
        }

        Subscription s = DataSource.getInstance().getAccountData(this, new BaseCallback<User>() {
            @Override
            public void onDataLoaded(User user) {
                setNavigationHeaderByUser(user);
            }

            @Override
            public void onDataNotAvailable(Error error) {
                if(count == 1 && error.getCode() == ErrorConstants.ACCESS_TOKEN_EXPIRED) {
                    AccountUtils.invalidateAuthToken(MainActivity.this, null);
                    setNavigationHeaderByAuth(2);
                } else {
                    ToastUtils.showShort(error.getDescription());
                    setNavigationHeaderByUser(null);
                }
            }
        });

        addSubscription(s);

    }

    private void setNavigationHeaderByUser(User user) {
        if(user == null) {
            setNoUserNavigationHeader();
        } else {
            ImageLoader.loadAvatarImage(user.getAvatar(), avatarImageView);
            accountNameTextView.setText(user.getName());
            loginTextView.setText(R.string.logout);

            avatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start UserDetailActivity
                }
            });
            loginTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountUtils.removeAccount(MainActivity.this);
                    setNoUserNavigationHeader();
                }
            });
        }

    }

    private void setNoUserNavigationHeader() {
        avatarImageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
        accountNameTextView.setText("");
        loginTextView.setText(R.string.login);

        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
            }
        };
        avatarImageView.setOnClickListener(loginListener);
        loginTextView.setOnClickListener(loginListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            setNavigationHeaderByAuth(1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
