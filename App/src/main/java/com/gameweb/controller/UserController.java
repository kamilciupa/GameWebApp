package com.gameweb.controller;

import com.gameweb.model.User;
import com.gameweb.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Created by Kamil on 2017-12-13.
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;

    ModelAndView modelAndView = new ModelAndView();

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registrationGet(){
        modelAndView.addObject("user", new User());
        modelAndView.addObject("p", "Rejestracja");
        modelAndView.setViewName("/registrationTest");
        return modelAndView;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(@Valid User user, BindingResult bindingResult){

        modelAndView.addObject("user", new User());
        if(bindingResult.hasErrors()){

            modelAndView.addObject("p", "Rejestracja zakończona z błedem");
        } else {
            userService.registration(user);
            String tet = user.toString();
            modelAndView.addObject("p", tet);
        }
        modelAndView.setViewName("/registrationTest");
        return  modelAndView;
    }


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
            s = new sun.misc.BASE64Encoder().encode(bytes);
            System.out.println(s);

        } catch (IOException e) {
            e.printStackTrace();
        }

        modelAndView.addObject("img", "data:image/png;base64,"+ s);
        modelAndView.setViewName("/uploadPictureTest");
        return  modelAndView;
    }


}
