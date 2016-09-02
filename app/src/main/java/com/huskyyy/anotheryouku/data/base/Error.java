package com.huskyyy.anotheryouku.data.base;

import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/8/11.
 */
public class Error {

    private long code;
    private String type;
    private String description;
    private Throwable e;

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error error = (Error) o;
        return code == error.code &&
                ObjectsUtils.equal(type, error.type) &&
                ObjectsUtils.equal(description, error.description);
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(code, type, description);
    }

    @Override
    public String toString() {
        return "Error{" +
                "code=" + code +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", e=" + e +
                '}';
    }
}
