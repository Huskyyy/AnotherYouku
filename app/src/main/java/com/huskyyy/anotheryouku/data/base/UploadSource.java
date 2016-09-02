package com.huskyyy.anotheryouku.data.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/7/4.
 */
public class UploadSource implements Parcelable {

    private int id;
    private String name;
    private String link;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        UploadSource source = (UploadSource) o;
        return id == source.getId() &&
                ObjectsUtils.equal(name, source.getName()) &&
                ObjectsUtils.equal(link, source.getLink());
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(String.valueOf(id), name, link);
    }

    @Override
    public String toString() {
        return "UploadSource{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", link='" + link + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.link);
    }

    public UploadSource() {
    }

    protected UploadSource(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.link = in.readString();
    }

    public static final Creator<UploadSource> CREATOR = new Creator<UploadSource>() {
        @Override
        public UploadSource createFromParcel(Parcel source) {
            return new UploadSource(source);
        }

        @Override
        public UploadSource[] newArray(int size) {
            return new UploadSource[size];
        }
    };
}
