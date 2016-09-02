package com.huskyyy.anotheryouku.activity.main.grid;

import com.huskyyy.anotheryouku.activity.base.BasePresenter;
import com.huskyyy.anotheryouku.activity.base.BaseView;

import rx.Subscription;

/**
 * Created by Wang on 2016/8/22.
 */
public interface GridContract {

    interface Presenter extends BasePresenter {

        void openCategory(String category);
    }

    interface View extends BaseView<Presenter> {

        void showCategory(String category);

        void showNoNetworkData();
    }
}
