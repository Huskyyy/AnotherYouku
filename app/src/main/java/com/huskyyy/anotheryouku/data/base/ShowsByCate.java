package com.huskyyy.anotheryouku.data.base;

/**
 * Created by Wang on 2016/7/22.
 */
public class ShowsByCate {

    private int total;
    private ShowData[] shows;

    public ShowData[] getShows() {
        return shows;
    }

    public void setShows(ShowData[] shows) {
        this.shows = shows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
