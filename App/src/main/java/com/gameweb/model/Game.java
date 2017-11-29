package com.gameweb.model;

/**
 * Created by Kamil on 2017-11-29.
 */
public class Game {


    private Integer id;
    private String title;
    private String about;
    private byte[] cover;
    private Double avgRating;
    private Integer masterId;

    private Game(){

    }

    public Game(String title, Integer masterId) {
        this.title = title;
        this.masterId = masterId;
        this.avgRating = 0.0d;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }
}
