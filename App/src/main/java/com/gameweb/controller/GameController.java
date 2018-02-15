package com.gameweb.controller;

import com.gameweb.model.Game;
import com.gameweb.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Kamil on 2017-11-29.
 */
@RestController
public class GameController {


    @Autowired
    GameService gameService;

    ModelAndView modelAndView = new ModelAndView();

    /*
     * Rejestracja
     */

    @RequestMapping(value = "/games/addGame", method = RequestMethod.GET)
    public ModelAndView registrationGet() {
        modelAndView.addObject("game", new Game());
        modelAndView.addObject("p", "Dodaj grę");
        modelAndView.setViewName("/addGame");
        return modelAndView;
    }



}
