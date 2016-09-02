package com.huskyyy.anotheryouku.data.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/7/4.
 */
public class User implements Parcelable {

    private long id;
    private String name;
    private String link;
    private String avatar;
    @SerializedName("avatar_large")
    private String avatarLarge;
    private String gender;
    private String description;
    @SerializedName("videos_count")
    private int videosCount;
    @SerializedName("playlists_count")
    private int playlistsCount;
    @SerializedName("favorites_count")
    private int favoritesCount;
    @SerializedName("followers_count")
    private int followersCount;
    @SerializedName("following_count")
    private int followingCount;
    @SerializedName("statuses_count")
    private int statusCount;
    @SerializedName("subscribe_count")
    private int subscribeCount;
    @SerializedName("vv_count")
    private long videoViewedCount;
    @SerializedName("regist_time")
    private String registedTime;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarLarge() {
        return avatarLarge;
    }

    public void setAvatarLarge(String avatarLarge) {
        this.avatarLarge = avatarLarge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getPlaylistsCount() {
        return playlistsCount;
    }

    public void setPlaylistsCount(int playlistsCount) {
        this.playlistsCount = playlistsCount;
    }

    public String getRegistedTime() {
        return registedTime;
    }

    public void setRegistedTime(String registedTime) {
        this.registedTime = registedTime;
    }

    public int getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(int statusCount) {
        this.statusCount = statusCount;
    }

    public int getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(int subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public int getVideosCount() {
        return videosCount;
    }

    public void setVideosCount(int videosCount) {
        this.videosCount = videosCount;
    }

    public long getVideoViewedCount() {
        return videoViewedCount;
    }

    public void setVideoViewedCount(long videoViewedCount) {
        this.videoViewedCount = videoViewedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.link);
        dest.writeString(this.avatar);
        dest.writeString(this.avatarLarge);
        dest.writeString(this.gender);
        dest.writeString(this.description);
        dest.writeInt(this.videosCount);
        dest.writeInt(this.playlistsCount);
        dest.writeInt(this.favoritesCount);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.followingCount);
        dest.writeInt(this.statusCount);
        dest.writeInt(this.subscribeCount);
        dest.writeLong(this.videoViewedCount);
        dest.writeString(this.registedTime);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.link = in.readString();
        this.avatar = in.readString();
        this.avatarLarge = in.readString();
        this.gender = in.readString();
        this.description = in.readString();
        this.videosCount = in.readInt();
        this.playlistsCount = in.readInt();
        this.favoritesCount = in.readInt();
        this.followersCount = in.readInt();
        this.followingCount = in.readInt();
        this.statusCount = in.readInt();
        this.subscribeCount = in.readInt();
        this.videoViewedCount = in.readLong();
        this.registedTime = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
