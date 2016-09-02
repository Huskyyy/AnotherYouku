package com.huskyyy.anotheryouku.data.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/7/4.
 */
public class BlockedReason implements Parcelable {
    private int no;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public int getNo() {
        return no;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(String.valueOf(no), desc);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        BlockedReason blockedReason = (BlockedReason) o;
        return no == blockedReason.getNo() &&
                ObjectsUtils.equal(desc, blockedReason.getDesc());
    }

    @Override
    public String toString() {
        return "BlockedReason{" +
                "desc='" + desc + '\'' +
                ", no=" + no +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.no);
        dest.writeString(this.desc);
    }

    public BlockedReason() {
    }

    protected BlockedReason(Parcel in) {
        this.no = in.readInt();
        this.desc = in.readString();
    }

    public static final Creator<BlockedReason> CREATOR = new Creator<BlockedReason>() {
        @Override
        public BlockedReason createFromParcel(Parcel source) {
            return new BlockedReason(source);
        }

        @Override
        public BlockedReason[] newArray(int size) {
            return new BlockedReason[size];
        }
    };
}
