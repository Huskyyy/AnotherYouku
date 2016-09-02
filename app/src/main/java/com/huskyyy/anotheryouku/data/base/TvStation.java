package com.huskyyy.anotheryouku.data.base;

import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/7/22.
 */
public class TvStation {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TvStation tvStation = (TvStation) o;
        return ObjectsUtils.equal(name, tvStation.name);
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(name);
    }

    @Override
    public String toString() {
        return "TvStation{" +
                "name='" + name + '\'' +
                '}';
    }
}
