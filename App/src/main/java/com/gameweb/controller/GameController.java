package com.gameweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Kamil on 2017-11-29.
 */
@Controller
public class GameController {


    @RequestMapping(value = "/test", method = RequestMethod.GET)
        public ModelAndView test(ModelAndView modelAndView){
        modelAndView.addObject("p", "TEST");
        modelAndView.setViewName("/test");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("Jestem aktywnym userem: " + currentPrincipalName);

        return  modelAndView;
    }



}
