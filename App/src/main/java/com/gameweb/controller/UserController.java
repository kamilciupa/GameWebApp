package com.gameweb.controller;

import com.gameweb.model.User;
import com.gameweb.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import java.io.IOException;




/**
 * Created by Kamil on 2017-12-13.
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;

    ModelAndView modelAndView = new ModelAndView();

/*
 * Rejestracja
 */

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registrationGet() {
        modelAndView.addObject("user", new User());
        modelAndView.addObject("p", "Rejestracja");
        modelAndView.setViewName("/registrationTest");
        return modelAndView;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(@Valid User user,BindingResult bindingResult) {
        modelAndView.addObject("user", new User());
        boolean hasErrors = false;
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("p", "B L A D ");
            hasErrors = true;
        }
        if(!userService.isUsernameFree(user.getUsername())) {
            modelAndView.addObject("p", "Nazwa użytkownika jest zajęta");
            hasErrors = true;
        }
        if(!userService.isEmailFree(user.getEmail())){
            modelAndView.addObject("p", "Email jest zajęty");
            hasErrors = true;
        }


       if(!hasErrors){
            modelAndView.addObject("p", "Sukces");
            userService.registration(user);
        }

        modelAndView.setViewName("/registrationTest");
        return  modelAndView;
    }

    /*
     * Logowanie
     */

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(){
        modelAndView.setViewName("/loginTestJsp");
        return modelAndView;
    }


    /*
     * Wylogowanie
     */

//    @RequestMapping(value="/logout", method = RequestMethod.GET)
//    public ModelAndView logoutPage (HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null){
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        modelAndView.setViewName("/loginTestJsp");
//        return modelAndView;
//    }

    /*
     * Dodawanie zdjęcia
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public ModelAndView uploadGet(){
        modelAndView.setViewName("/uploadPictureTest");
        return  modelAndView;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadPost(@RequestParam("file") MultipartFile uploadfile){

        modelAndView.addObject("file" , uploadfile);
        String s = "";
        try {
            byte[] bytes = uploadfile.getBytes();
            org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
            s = encoder.encodeToString(bytes);
            System.out.println(s);

        } catch (IOException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("img", "data:image/png;base64,"+ s);
        modelAndView.setViewName("/uploadPictureTest");
        return  modelAndView;
    }
}
