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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
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
    public ModelAndView registration(@Valid Game game, @RequestPart(value = "file", required = false) MultipartFile file, BindingResult bindingResult, HttpServletRequest request) throws IOException {
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
            game.setCover(file.getBytes());
            System.out.println(file.getName());
            modelAndView.addObject("p", "Sukces");
            gameService.addGame(game, user.getId());
            modelAndView.setViewName("/profile");
        } else {
            modelAndView.setViewName("/registrationTest");
        }


        return  modelAndView;
    }

    @RequestMapping(value = "games/profile/{gameTitle}", method = RequestMethod.GET)
    public ModelAndView getProfileGame(@PathVariable("gameTitle") String gameTitle){
        Game game = gameService.getGameByTitle(gameTitle);


        String s = "";
        byte[] bytes;
        try {
            if(game.getCover() == null){
                bytes = userService.getDefaultAvatar();
            } else {
                bytes = game.getCover();
            }
            org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
            s = encoder.encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelAndView.addObject("img", "data:image/png;base64,"+ s);
        modelAndView.addObject("title", game.getTitle());
        modelAndView.addObject("about", game.getAbout());
        modelAndView.addObject("developer", game.getDeveloper());
        modelAndView.addObject("releaseDate",game.getReleaseDate());
        modelAndView.setViewName("/gameProfile");
        return modelAndView;
    }


}
