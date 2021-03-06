package com.gameweb.model;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * Created by Kamil on 2017-11-29.
 */

@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    private String username;
    private String email;
    private String password;
    private byte[] avatar; // niewymagany
    private String about; // niewymagany

    private ArrayList<Game> userGames;


    //private CommonsMultipartFile file;

    public User(){

    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ArrayList<Game> getUserGames() {
        return userGames;
    }

    public void setUserGames(ArrayList<Game> userGames) {
        this.userGames = userGames;
    }

    //    public CommonsMultipartFile getFile() {
//        return file;
//    }
//
//    public void setFile(CommonsMultipartFile file) {
//        this.file = file;
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatar=" + Arrays.toString(avatar) +
                ", about='" + about + '\'' +
                '}';
    }
}
