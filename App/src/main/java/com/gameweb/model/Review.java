package com.gameweb.model;



/**
 * Created by Kamil on 2017-11-29.
 */

public class Review {

    private Integer id;
    private Integer parentId;
    private String content;
    private Double rating;

    private Review(){

    }

    public Review(Integer parentId, String content) {
        this.parentId = parentId;
        this.content = content;
        this.rating = 0.0d;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
