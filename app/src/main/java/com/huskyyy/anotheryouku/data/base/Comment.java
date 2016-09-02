package com.huskyyy.anotheryouku.data.base;

import com.huskyyy.anotheryouku.util.ObjectsUtils;

import java.util.Objects;

/**
 * Created by Wang on 2016/8/3.
 */
public class Comment {

    private String id;
    private String content;
    private String published;
    private User user;
    private VideoData video;
    private UploadSource source;
    private boolean sendByAccount;

    public boolean isSendByAccount() {
        return sendByAccount;
    }

    public void setSendByAccount(boolean sendByAccount) {
        this.sendByAccount = sendByAccount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public UploadSource getSource() {
        return source;
    }

    public void setSource(UploadSource source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VideoData getVideo() {
        return video;
    }

    public void setVideo(VideoData video) {
        this.video = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return ObjectsUtils.equal(id, comment.id);
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "user=" + user +
                ", id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
