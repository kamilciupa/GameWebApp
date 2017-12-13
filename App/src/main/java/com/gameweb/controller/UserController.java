package com.gameweb.controller;

import com.gameweb.model.User;
import com.gameweb.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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
   // public ModelAndView registration(@RequestBody User user, BindingResult bindingResult){
//    public ModelAndView registration(@ModelAttribute User user, BindingResult bindingResult){

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

}
