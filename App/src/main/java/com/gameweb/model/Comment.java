package com.gameweb.model;

public class Comment {

    private int id;
    private String content;
    private int parent;
    private String type; // G - gra R - recenzja
    private int key_value;
    private int author;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getKey_value() {
        return key_value;
    }

    public void setKey_value(int key_value) {
        this.key_value = key_value;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }
}
