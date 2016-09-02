package com.huskyyy.anotheryouku.data.base;

/**
 * Created by Wang on 2016/7/22.
 */
public class VideosByCategory {

    private int total;
    private int page;
    private int count;
    private String category;
    private String genre;
    private String tag;
    private String orderBy;
    private int logoId;
    private VideoData[] videos;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public VideoData[] getVideos() {
        return videos;
    }

    public void setVideos(VideoData[] videos) {
        this.videos = videos;
    }
}
