package com.huskyyy.anotheryouku.activity.base.activity;

/**
 * Created by Wang on 2016/7/29.
 */
public class SwipeRefreshBaseActivity extends BaseActivity {

//    @BindView(R.id.swipe_refresh_layout)
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private boolean refreshing;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
//        setupSwipeRefreshLayout();
//    }
//
//    private void setupSwipeRefreshLayout() {
//        if(swipeRefreshLayout != null) {
//            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    loadData();
//                }
//            });
//        }
//    }
//
//    protected void loadData() {
//
//    }
//
//    protected void startRefreshing(){
//
//        refreshing = true;
//        if(swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
//            swipeRefreshLayout.setRefreshing(true);
//        }
//    }
//
//    protected void stopRefreshing(){
//
//        refreshing = false;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(refreshing == false && swipeRefreshLayout != null
//                        && swipeRefreshLayout.isRefreshing()){
//                    swipeRefreshLayout.setRefreshing(false);
//                }
//            }
//        }, 1000);
//    }

}
