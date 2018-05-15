package com.gameweb.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by Kamil on 2017-11-29.
 */
public class Game {


    private Integer id;
    private String title;
    private String about;
    private byte[] cover;
    private String developer;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    private Double rating;
    private Integer votesAmount;
    private Integer votesSum;
    private Integer masterId;


    private int cat_id;
    private String cat_name;
    private String coverByteString;

    public String getCoverByteString() {
        return coverByteString;
    }

    public void setCoverByteString(String coverByteString) {
        this.coverByteString = coverByteString;
    }

    public Game(){

    }

    public Game(String title, Integer masterId) {
        this.title = title;
        this.masterId = masterId;
        this.rating = 0.0d;
        this.votesAmount = 0;
        this.votesSum = 0;
    }


    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
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

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getVotesAmount() {
        return votesAmount;
    }

    public void setVotesAmount(Integer votesAmount) {
        this.votesAmount = votesAmount;
    }

    public Integer getVotesSum() {
        return votesSum;
    }

    public void setVotesSum(Integer votesSum) {
        this.votesSum = votesSum;
    }
}
