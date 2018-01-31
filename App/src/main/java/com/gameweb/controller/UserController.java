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
        try {
            if(user.getAvatar() == null){
                s = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCAFeAdMDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD6pooooAKKKKACiiigAooooAKY3U0+mHqaAEooooAKKKKACl+tJn0qC8fyrWWT+6pNNK7sJuyufNH7Unjt2nTwzp7EFsGcjvnov9a2/gL8ObaLw6bu+jzJcLnmvBPGV3Jr3xVmeQk77ogZ9AcCvt3wNbLaeG7OIDGIxVZrK2Jp4SO0Fd+oYeKlQdWW8vyPmnx18J7a58YyW0B8sSkla4rxB8H9f0KUy2cbSIvIK19IePUEfjXT3jPzlhmvRBAk1oBKitle4rWvVTrcs1dNJmdODjTUoOzVz4ETxJ4o0KZokvLq3dTghWIrotN+N3jHT4gn24yAd5EBNdP8ZtMgtvE04SNQCc4ArzZ7GFzyorhxns8LV5LeZ9Pl+UVsdh414z37noel/tIeJrfAuYref6qQa2rb9pzVVb9/pkLDPZyOK8s8P6Fa3uvWtvIvyOwBr6Vtvgf4euLGJzH8zKDWdGtCrPkXqebj8NVwU+SaTOOl/aik8rCaP8/vLx/KtrwD+0K+v6/FZX1lHbwyHG/zMkfpUPin4K+HtK0ya4P3gPlHqa+e7C0OieNoF2lIxKMZ9M104mjNYaVSD1Rx0K0ZVVCUdz9FYJFmiWRDlWGRUlZHhOcXPh+ykU5BjH8q16yw9T2tKM+6JnHlk49gooorYkKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACmHrT6MUAR0VJijFAEdJ1qXFGKAI6r6ghkspkHUqRVzFIwBGKadncTV1Y/PjxKj6J8TZTOCDHdHqP9qvt7wNqCX3hq0mTBHlj+VfPX7TvgC4h1MeILCMtE+PN2j7pHeofhT8YoNH0T+zdQ4lUbVzWma03LFQxcNYyWvkLBT5qDov4onqGprJqvxFQLzHCa9Qxsh57CvPvhy0V/NLqssiF5jkDPSut8TapFpukTzu4ACnHNZRksRXc47aJfIpp04KLPmD403CzeKZ9pzg4rzqtrxdqR1PWrmcnIZzisWuDNqiniHbpofpmQ0HQwMIy3N74fx+b4wslIyN4r7SglS205HcgKqZr5P8Aglpv2/xWkrLlI+c19B+K9VadF0vT8vI/ysV7VGXQ5q0pPZI+R4hq82IaiZt3NN4s1nyYs/Yojyexr57+O+nDSPGlu8a7UDCvrPwrpEek6aikASEZY187/tUR2xnhljdTKDyAa97CP2/tFLZppHzlT93ODXRnvvwf1FdR8FWLhgSqAGu4rwH9ljXBdaA9mz5ZOQM179Xh5dL9zyPeLaO3FK1RtddQooor0DnCiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooApatpttqtlJa3kayQyKVKsM180/Er9nhzM974Wkx1Ywuf5GvqOgitqdeVNcu67GU6Sk+ZaPufBBu/G/geb7PNHdRBegYHH51DrfxS8Raja/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";
            } else {
                byte[] bytes = user.getAvatar();
                org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
                s = encoder.encodeToString(bytes);
                System.out.println("długośc bajtów" + bytes.length);
                System.out.println(s);
            }
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
