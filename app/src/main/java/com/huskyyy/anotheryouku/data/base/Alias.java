package com.huskyyy.anotheryouku.data.base;

import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/7/22.
 */
public class Alias {
    private String type;
    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
        Alias alias1 = (Alias) o;
        return ObjectsUtils.equal(type, alias1.type) &&
                ObjectsUtils.equal(alias, alias1.alias);
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(type, alias);
    }

    @Override
    public String toString() {
        return "Alias{" +
                "type='" + type + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
