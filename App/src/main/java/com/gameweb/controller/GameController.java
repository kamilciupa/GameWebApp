package com.gameweb.controller;

import com.gameweb.model.Game;
import com.gameweb.model.Review;
import com.gameweb.model.User;
import com.gameweb.service.GameService;
import com.gameweb.service.ReviewService;
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
import java.util.ArrayList;
import java.util.List;




/**
 * Created by Kamil on 2017-11-29.
 */
@RestController
public class GameController {


    @Autowired
    GameService gameService;
    @Autowired
    UserService userService;
    @Autowired
    ReviewService reviewService;

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

    @RequestMapping(value = "games/all", method = RequestMethod.GET)
    public ModelAndView getAllGames(){
        List<Game> games = new ArrayList<Game>();
        games = gameService.getGamesTitles();
        System.out.println(games.size());
        System.out.println(games.get(1));
        modelAndView.addObject("games", games);
        modelAndView.setViewName("/listGame");
        return modelAndView;
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
        modelAndView.addObject("rating", Math.round(game.getRating()));
        modelAndView.addObject("votes_amount", game.getVotesAmount());
        modelAndView.setViewName("/gameProfile");
        return modelAndView;
    }

    @RequestMapping(value = "games/profile/{gameTitle}/addReview", method = RequestMethod.GET)
    public ModelAndView getAddReview(@PathVariable("gameTitle") String gameTitle){
        Game game = gameService.getGameByTitle(gameTitle);
        modelAndView.addObject("review", new Review());
        modelAndView.addObject("title", game.getTitle());
        modelAndView.setViewName("/addReview");
        return modelAndView;
    }


    @RequestMapping(value = "/games/profile/{gameTitle}/addReview", method = RequestMethod.POST)
    public ModelAndView addReviewPost(@Valid Review review,@PathVariable("gameTitle") String gameTitle, BindingResult bindingResult, HttpServletRequest request) throws IOException {

        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByName( principal.getName());
        Game game =  gameService.getGameByTitle(gameTitle);
        modelAndView.addObject("review", new Review());

        boolean hasErrors = false;
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("p", "B L A D ");
            hasErrors = true;
        }
        System.out.println(review.getReviewTitle());System.out.println(review.getReviewTitle());System.out.println(review.getReviewTitle());
        if(!hasErrors){
            review.setParentId(user.getId());
            review.setKey_value(game.getId());
            reviewService.addReview(review);
            modelAndView.setViewName("/addReview");
        } else {
            modelAndView.setViewName("/registrationTest");
        }

        return  modelAndView;
    }

    //TODO Przenieść to do kontrolera od recenzji -> Stworzyć kontroler od recenzji

    @RequestMapping(value = "/reviews/{id}", method = RequestMethod.GET)
    public ModelAndView getWholeReview(@PathVariable("id") Integer reviewId){
        Review review = reviewService.getReviewById(reviewId);
        modelAndView.addObject("content", review.getContent());
        modelAndView.addObject("title" , review.getReviewTitle());
        modelAndView.addObject("author", review.getAuthorName());
        modelAndView.addObject("gameTitle", review.getGameName());

        modelAndView.setViewName("/wholeReview");
        return  modelAndView;
    }

    @RequestMapping(value = "/games/profile/{gameTitle}/vote/{vote}", method = RequestMethod.GET)
    public ModelAndView addVote(@PathVariable("gameTitle") String gameTitle, @PathVariable("vote") Integer vote){
        Game game = gameService.getGameByTitle(gameTitle);
        game.setVotesAmount(game.getVotesAmount()+1);
        game.setVotesSum(game.getVotesSum()+vote);
        game.setRating( game.getVotesSum().doubleValue()  / game.getVotesAmount().doubleValue());
        gameService.updateGameRating(game);
        modelAndView.clear();
        modelAndView.setViewName("redirect:/games/profile/"+gameTitle.replace(" ","%20"));
        return modelAndView;
    }

}
