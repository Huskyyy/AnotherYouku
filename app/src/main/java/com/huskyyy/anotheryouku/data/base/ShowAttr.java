package com.huskyyy.anotheryouku.data.base;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Wang on 2016/7/22.
 */
public class ShowAttr {

    private Director[] director;
    private ScreenWriter[] screenwriter;
    private ShowProducer[] producer;
    private Starring[] starring;
    private Performer[] performer;
    private Original[] original;
    private Host[] host;
    @SerializedName("tv_station")
    private TvStation tvStation;
    private ShowVoice[] voice;
    private Singer[] singer;
    private LyricsWriter[] lyricswriter;
    private Composer[] composer;
    private Teacher[] teacher;

    public Composer[] getComposer() {
        return composer;
    }

    public void setComposer(Composer[] composer) {
        this.composer = composer;
    }

    public Director[] getDirector() {
        return director;
    }

    public void setDirector(Director[] director) {
        this.director = director;
    }

    public Host[] getHost() {
        return host;
    }

    public void setHost(Host[] host) {
        this.host = host;
    }

    public LyricsWriter[] getLyricswriter() {
        return lyricswriter;
    }

    public void setLyricswriter(LyricsWriter[] lyricswriter) {
        this.lyricswriter = lyricswriter;
    }

    public Original[] getOriginal() {
        return original;
    }

    public void setOriginal(Original[] original) {
        this.original = original;
    }

    public Performer[] getPerformer() {
        return performer;
    }

    public void setPerformer(Performer[] performer) {
        this.performer = performer;
    }

    public ShowProducer[] getProducer() {
        return producer;
    }

    public void setProducer(ShowProducer[] producer) {
        this.producer = producer;
    }

    public ScreenWriter[] getScreenwriter() {
        return screenwriter;
    }

    public void setScreenwriter(ScreenWriter[] screenwriter) {
        this.screenwriter = screenwriter;
    }

    public Singer[] getSinger() {
        return singer;
    }

    public void setSinger(Singer[] singer) {
        this.singer = singer;
    }

    public Starring[] getStarring() {
        return starring;
    }

    public void setStarring(Starring[] starring) {
        this.starring = starring;
    }

    public Teacher[] getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher[] teacher) {
        this.teacher = teacher;
    }

    public TvStation getTvStation() {
        return tvStation;
    }

    public void setTvStation(TvStation tvStation) {
        this.tvStation = tvStation;
    }

    public ShowVoice[] getVoice() {
        return voice;
    }

    public void setVoice(ShowVoice[] voice) {
        this.voice = voice;
    }
}
