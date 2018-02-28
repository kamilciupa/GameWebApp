package com.gameweb.model;



/**
 * Created by Kamil on 2017-11-29.
 */

public class Review {

    private Integer id;
    private Integer parentId; // ID Usera
    private String content;
    private String reviewTitle;
    private Integer key_value; // Id Gry
    private Double rating;
    private String gameName;
    private String authorName;

    public Review(){

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

    public Integer getKey_value() {
        return key_value;
    }

    public void setKey_value(Integer key_value) {
        this.key_value = key_value;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
