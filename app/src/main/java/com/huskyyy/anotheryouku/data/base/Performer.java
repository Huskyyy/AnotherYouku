package com.huskyyy.anotheryouku.data.base;

/**
 * Created by Wang on 2016/7/22.
 */
public class Performer extends Person {

    private String character;

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return "Performer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
