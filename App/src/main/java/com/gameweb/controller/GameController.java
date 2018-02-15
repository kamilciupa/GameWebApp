package com.gameweb.controller;

import com.gameweb.model.Game;
import com.gameweb.model.User;
import com.gameweb.service.GameService;
import com.gameweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Kamil on 2017-11-29.
 */
@RestController
public class GameController {


    @Autowired
    GameService gameService;
    @Autowired
    UserService userService;

    ModelAndView modelAndView = new ModelAndView();

    /*
     * Rejestracja
     */

    @RequestMapping(value = "/games/addGame", method = RequestMethod.GET)
    public ModelAndView addGameShow() {
        modelAndView.addObject("game", new Game());
        modelAndView.addObject("p", "Dodaj grę");
        modelAndView.setViewName("/addGame");
        return modelAndView;
    }

    @RequestMapping(value = "/games/addGame", method = RequestMethod.POST)
    public ModelAndView registration(@Valid Game game, BindingResult bindingResult, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByName( principal.getName());
        modelAndView.addObject("game", new Game());
        boolean hasErrors = false;
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("p", "B L A D ");
            hasErrors = true;
        }

//        TODO walidacje
//        if(!userService.isUsernameFree(user.getUsername())) {
//            modelAndView.addObject("p", "Nazwa użytkownika jest zajęta");
//            hasErrors = true;
//        }
//        if(!userService.isEmailFree(user.getEmail())){
//            modelAndView.addObject("p", "Email jest zajęty");
//            hasErrors = true;
//        }


        if(!hasErrors){
            modelAndView.addObject("p", "Sukces");
            gameService.addGame(game, user.getId());
            modelAndView.setViewName("/profile");
        } else {
            modelAndView.setViewName("/registrationTest");
        }


        return  modelAndView;
    }



}
