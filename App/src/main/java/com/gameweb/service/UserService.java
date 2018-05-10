package com.gameweb.service;

import com.gameweb.DAO.UserDAO;
import com.gameweb.model.Game;
import com.gameweb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by Kamil on 2017-12-13.
 **/
@Service
public class UserService {

    @Autowired
    UserDAO userDAO;
    @Autowired
    PasswordEncoder passwordEncoder;
    /*----- Registration methods -----*/

    public ResponseEntity registration(User userInput){
        User user = userInput;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.addUserToDB(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    public boolean isUsernameFree(String username) {
        List<User> tmp = userDAO.getAllUsers();
        for(User t : tmp){
            if(t.getUsername().equals(username)){
                return false;
            }
        }
        return true;
    }

    public boolean isEmailFree(String email) {
        List<User> tmp = userDAO.getAllUsers();
        for(User t : tmp){
            if(t.getEmail().equals(email)){
                return false;
            }
        }
        return true;
    }

    /*----- Registration methods -----*/

    public User getUserByName(String username){
        User user = userDAO.getUserByName(username);
        return user;
    }

    public void updateAbout(User user) {
        userDAO.updateAbout(user);
    }

    public ArrayList<Game> getUserGames(User user)
    {
        return userDAO.getUserGames(user);
    }
    public void updateUserInfo(User user){
        userDAO.updateUserInfo(user);
    }

    public void updateAvatar(User user){
        userDAO.updateAvatar(user);
    }

    public static byte[] getDefaultAvatar(){
        return userDAO.getDefaultAvatar();
    }

}
