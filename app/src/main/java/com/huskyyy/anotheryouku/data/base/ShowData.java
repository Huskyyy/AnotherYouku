package com.huskyyy.anotheryouku.data.base;

import com.google.gson.annotations.SerializedName;
import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/7/22.
 */
public class ShowData {

    private String id;
    private String name;
    private Alias[] alias;
    private String link;
    @SerializedName("play_link")
    private String playLink;
    private String poster;
    @SerializedName("poster_large")
    private String posterLarge;
    private String thumbnail;
    @SerializedName("streamtypes")
    private String[] streamTypes;
    private String genre;
    private String area;
    @SerializedName("episode_count")
    private int episodeCount;
    @SerializedName("episode_updated")
    private int episodeUpdated;
    @SerializedName("view_count")
    private int viewCount;
    private float score;
    private int paid;
    private String released;
    private String published;
    private String category;
    private String description;
    private int rank;
    @SerializedName("view_yesterday_count")
    private int viewYesterdayCount;
    @SerializedName("view_week_count")
    private int viewWeekCount;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("favorite_count")
    private int favoriteCount;
    @SerializedName("up_count")
    private int upCount;
    @SerializedName("down_count")
    private int downCount;
    private ShowAttr attr;
    private String state;
    @SerializedName("copyright_status")
    private String copyrightStatus;

    public Alias[] getAlias() {
        return alias;
    }

    public void setAlias(Alias[] alias) {
        this.alias = alias;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public ShowAttr getAttr() {
        return attr;
    }

    public void setAttr(ShowAttr attr) {
        this.attr = attr;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCopyrightStatus() {
        return copyrightStatus;
    }

    public void setCopyrightStatus(String copyrightStatus) {
        this.copyrightStatus = copyrightStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDownCount() {
        return downCount;
    }

    public void setDownCount(int downCount) {
        this.downCount = downCount;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public int getEpisodeUpdated() {
        return episodeUpdated;
    }

    public void setEpisodeUpdated(int episodeUpdated) {
        this.episodeUpdated = episodeUpdated;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getPlayLink() {
        return playLink;
    }

    public void setPlayLink(String playLink) {
        this.playLink = playLink;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPosterLarge() {
        return posterLarge;
    }

    public void setPosterLarge(String posterLarge) {
        this.posterLarge = posterLarge;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String[] getStreamTypes() {
        return streamTypes;
    }

    public void setStreamTypes(String[] streamTypes) {
        this.streamTypes = streamTypes;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getUpCount() {
        return upCount;
    }

    public void setUpCount(int upCount) {
        this.upCount = upCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getViewWeekCount() {
        return viewWeekCount;
    }

    public void setViewWeekCount(int viewWeekCount) {
        this.viewWeekCount = viewWeekCount;
    }

    public int getViewYesterdayCount() {
        return viewYesterdayCount;
    }

    public void setViewYesterdayCount(int viewYesterdayCount) {
        this.viewYesterdayCount = viewYesterdayCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowData showData = (ShowData) o;
        return ObjectsUtils.equal(id, showData.id);
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShowData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
