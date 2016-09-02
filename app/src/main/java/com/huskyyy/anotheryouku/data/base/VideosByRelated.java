package com.huskyyy.anotheryouku.data.base;

/**
 * Created by Wang on 2016/8/6.
 */
public class VideosByRelated {

    private int total;
    private int apptype;
    private int pg;
    private int module;
    private String ver;
    private String ord;
    private VideoData[] videos;

    public int getApptype() {
        return apptype;
    }

    public void setApptype(int apptype) {
        this.apptype = apptype;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getOrd() {
        return ord;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }

    public int getPg() {
        return pg;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public VideoData[] getVideos() {
        return videos;
    }

    public void setVideos(VideoData[] videos) {
        this.videos = videos;
    }
}
