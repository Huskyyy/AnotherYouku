package com.huskyyy.anotheryouku.data.base;

import com.huskyyy.anotheryouku.util.ObjectsUtils;

/**
 * Created by Wang on 2016/7/22.
 */
public class Person {

    protected String id;
    protected String name;
    protected String link;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person director = (Person) o;
        return ObjectsUtils.equal(id, director.id) &&
                ObjectsUtils.equal(name, director.name);
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hashCode(id, name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
