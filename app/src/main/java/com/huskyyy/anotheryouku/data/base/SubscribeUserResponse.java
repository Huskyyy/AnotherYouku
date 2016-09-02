package com.huskyyy.anotheryouku.data.base;

import java.util.Arrays;

/**
 * Created by Wang on 2016/8/15.
 */
public class SubscribeUserResponse {

    private int total;
    private int page;
    private int count;
    private User[] users;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "SubscribeUserResponse{" +
                "count=" + count +
                ", total=" + total +
                ", page=" + page +
                ", users=" + Arrays.toString(users) +
                '}';
    }
}
