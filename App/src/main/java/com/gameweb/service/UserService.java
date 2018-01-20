package com.gameweb.service;

import com.gameweb.DAO.UserDAO;
import com.gameweb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    BCryptPasswordEncoder bCryptPasswordEncoder;
    /*----- Registration methods -----*/

    public ResponseEntity registration(User userInput){
        User user = userInput;
        // pierwsza wersja
       // user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        // druga wersja
        System.out.println(user.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());

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



}
