package com.huskyyy.anotheryouku.data.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/7/3.
 */
public class VideoData implements Parcelable {

    private String id;
    private String title;
    private String link;
    private String thumbnail;
    private String bigThumbnail;
    private String duration;
    private String category;
    private String state;
    private String created;
    private String published;
    private String description;
    private String player;
    @SerializedName("public_type")
    private String publicType;
    @SerializedName("copyright_type")
    private String copyrightType;
    private User user;
    private String tags;
    @SerializedName("view_count")
    private int viewCount;
    @SerializedName("favorite_count")
    private String favoriteCount;
    @SerializedName("comment_count")
    private String commentCount;
    @SerializedName("up_count")
    private String upCount;
    @SerializedName("down_count")
    private String downCount;
    @SerializedName("operation_limit")
    private String[] operationLimit;
    @SerializedName("streamtypes")
    private String[] streamTypes;
    @SerializedName("blocked_reason")
    private BlockedReason blockedReason;
    private UploadSource source;
    @SerializedName("reference_count")
    private int referenceCount;

    public String getBigThumbnail() {
        return bigThumbnail;
    }

    public void setBigThumbnail(String bigThumbnail) {
        this.bigThumbnail = bigThumbnail;
    }

    public BlockedReason getBlockedReason() {
        return blockedReason;
    }

    public void setBlockedReason(BlockedReason blockedReason) {
        this.blockedReason = blockedReason;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCopyrightType() {
        return copyrightType;
    }

    public void setCopyrightType(String copyrightType) {
        this.copyrightType = copyrightType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownCount() {
        return downCount;
    }

    public void setDownCount(String downCount) {
        this.downCount = downCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(String favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String[] getOperationLimit() {
        return operationLimit;
    }

    public void setOperationLimit(String[] operationLimit) {
        this.operationLimit = operationLimit;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPublicType() {
        return publicType;
    }

    public void setPublicType(String publicType) {
        this.publicType = publicType;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public int getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(int referenceCount) {
        this.referenceCount = referenceCount;
    }

    public UploadSource getSource() {
        return source;
    }

    public void setSource(UploadSource source) {
        this.source = source;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpCount() {
        return upCount;
    }

    public void setUpCount(String upCount) {
        this.upCount = upCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoData videoData = (VideoData) o;
        return ObjectsUtils.equal(id, videoData.id);
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(id);
    }

    @Override
    public String toString() {
        return "VideoData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeString(this.thumbnail);
        dest.writeString(this.bigThumbnail);
        dest.writeString(this.duration);
        dest.writeString(this.category);
        dest.writeString(this.state);
        dest.writeString(this.created);
        dest.writeString(this.published);
        dest.writeString(this.description);
        dest.writeString(this.player);
        dest.writeString(this.publicType);
        dest.writeString(this.copyrightType);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.tags);
        dest.writeInt(this.viewCount);
        dest.writeString(this.favoriteCount);
        dest.writeString(this.commentCount);
        dest.writeString(this.upCount);
        dest.writeString(this.downCount);
        dest.writeStringArray(this.operationLimit);
        dest.writeStringArray(this.streamTypes);
        dest.writeParcelable(this.blockedReason, flags);
        dest.writeParcelable(this.source, flags);
        dest.writeInt(this.referenceCount);
    }

    public VideoData() {
    }

    protected VideoData(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.link = in.readString();
        this.thumbnail = in.readString();
        this.bigThumbnail = in.readString();
        this.duration = in.readString();
        this.category = in.readString();
        this.state = in.readString();
        this.created = in.readString();
        this.published = in.readString();
        this.description = in.readString();
        this.player = in.readString();
        this.publicType = in.readString();
        this.copyrightType = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.tags = in.readString();
        this.viewCount = in.readInt();
        this.favoriteCount = in.readString();
        this.commentCount = in.readString();
        this.upCount = in.readString();
        this.downCount = in.readString();
        this.operationLimit = in.createStringArray();
        this.streamTypes = in.createStringArray();
        this.blockedReason = in.readParcelable(BlockedReason.class.getClassLoader());
        this.source = in.readParcelable(UploadSource.class.getClassLoader());
        this.referenceCount = in.readInt();
    }

    public static final Creator<VideoData> CREATOR = new Creator<VideoData>() {
        @Override
        public VideoData createFromParcel(Parcel source) {
            return new VideoData(source);
        }

        @Override
        public VideoData[] newArray(int size) {
            return new VideoData[size];
        }
    };
}
