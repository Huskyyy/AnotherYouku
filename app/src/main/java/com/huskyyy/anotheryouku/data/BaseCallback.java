package com.huskyyy.anotheryouku.data;

import com.huskyyy.anotheryouku.data.base.Error;

/**
 * Created by Wang on 2016/8/12.
 */
public interface BaseCallback<T> {

    void onDataLoaded(T t);

    void onDataNotAvailable(Error error);
}
