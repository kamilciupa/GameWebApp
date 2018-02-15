package com.gameweb.controller;

import com.gameweb.model.User;
import com.gameweb.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
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
import java.security.Principal;


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
           modelAndView.setViewName("/loginTestJsp");
        } else {
           modelAndView.setViewName("/registrationTest");
       }


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
        modelAndView.setViewName("/settings");
        return  modelAndView;
    }

    /*
     * Profil użytkownika
     */
    @RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
    public ModelAndView getProfile(@PathVariable("username") String username){
        User user = userService.getUserByName(username);

        String s = "";
        byte[] bytes;
        try {
            if(user.getAvatar() == null){
                bytes = userService.getDefaultAvatar();
            } else {
                bytes = user.getAvatar();
            }
            org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
            s = encoder.encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }


        modelAndView.addObject("img", "data:image/png;base64,"+ s);
        modelAndView.addObject("username", user.getUsername());
        modelAndView.addObject("email", user.getEmail());
        modelAndView.addObject("about", user.getAbout());
        modelAndView.setViewName("/profile");
        return modelAndView;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView getSettings( HttpServletRequest request){

            Principal principal = request.getUserPrincipal();

        User user = userService.getUserByName( principal.getName());


        String s = "";
        try {
            byte[] bytes = user.getAvatar();
            org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
            s = encoder.encodeToString(bytes);
            System.out.println(s);
        } catch (Exception e){

        }
        modelAndView.addObject("user", user);
        modelAndView.addObject("img", "data:image/png;base64,"+ s);
        modelAndView.addObject("username", user.getUsername());
        modelAndView.addObject("about", user.getAbout());
        modelAndView.setViewName("/changeUserInfo");
        return modelAndView;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public ModelAndView postSettings(@Valid User user,  HttpServletRequest request, BindingResult bindingResult){


        modelAndView.addObject("user", new User());

        Principal principal = request.getUserPrincipal();

       User userOld = userService.getUserByName( principal.getName());
       userOld.setAbout(user.getAbout());

        if(!bindingResult.hasErrors()) {
            userService.updateAbout(userOld);
        }


        //modelAndView.addObject("img", "data:image/png;base64,"+ s);
        modelAndView.addObject("username", user.getUsername());
        modelAndView.addObject("about", user.getAbout());
        modelAndView.setViewName("/changeUserInfo");
        return modelAndView;
    }


    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    public ModelAndView updateAvatar(@RequestParam("file") MultipartFile uploadfile, HttpServletRequest request){

      //  modelAndView.addObject("file" , uploadfile);
        String s = "";
        try {
            byte[] bytes = uploadfile.getBytes();
            Principal principal = request.getUserPrincipal();

            User userOld = userService.getUserByName( principal.getName());
            userOld.setAvatar(bytes);
System.out.println("długośc bajta w upload avatar" + bytes.length);
            userService.updateAvatar(userOld);

        } catch (IOException e) {
            e.printStackTrace();
        }
       // modelAndView.addObject("img", "data:image/png;base64,"+ s);
        modelAndView.setViewName("/changeUserInfo");
        return  modelAndView;
    }


}
