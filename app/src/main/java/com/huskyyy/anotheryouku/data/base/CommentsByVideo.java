package com.huskyyy.anotheryouku.data.base;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Wang on 2016/8/3.
 */
public class CommentsByVideo {

    private String total;
    private int page;
    private int count;
    @SerializedName("video_id")
    private String videoId;
    private Comment[] comments;

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
