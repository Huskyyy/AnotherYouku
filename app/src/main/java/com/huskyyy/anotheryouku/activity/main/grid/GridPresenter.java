package com.huskyyy.anotheryouku.activity.main.grid;

import com.huskyyy.anotheryouku.util.NetUtils;

/**
 * Created by Wang on 2016/8/22.
 */
public class GridPresenter implements GridContract.Presenter {

    private GridContract.View gridView;

    public GridPresenter(GridContract.View gridView) {
        this.gridView = gridView;
        gridView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void openCategory(String category) {
        if(!NetUtils.isNetworkAvailable()) {
            gridView.showNoNetworkData();
            return;
        }
        gridView.showCategory(category);
    }
}
