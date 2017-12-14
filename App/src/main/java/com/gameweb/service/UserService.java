package com.gameweb.service;

import com.gameweb.DAO.UserDAO;
import com.gameweb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * Created by Kamil on 2017-12-13.
 **/
@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    /*----- Registration methods -----*/
    public ResponseEntity registration(User userInput){

        User user = userInput;
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userDAO.addUserToDB(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    /*----- Registration methods -----*/



}
