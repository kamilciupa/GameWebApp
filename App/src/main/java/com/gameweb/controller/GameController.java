package com.gameweb.controller;

import com.gameweb.model.Comment;
import com.gameweb.model.Game;
import com.gameweb.model.Review;
import com.gameweb.model.User;
import com.gameweb.service.CommentService;
import com.gameweb.service.GameService;
import com.gameweb.service.ReviewService;
import com.gameweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
public class GameController {


    @Autowired
    GameService gameService;
    @Autowired
    UserService userService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    CommentService commentService;

    ModelAndView modelAndView = new ModelAndView();

    @RequestMapping(value = "/games/addGame", method = RequestMethod.GET)
    public ModelAndView addGameShow() {
        modelAndView.addObject("game", new Game());
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
            gameService.addGame(game, user.getId());
            modelAndView.setViewName("/profile");
            modelAndView.clear();
            modelAndView.setViewName("redirect:/games/profile/" + game.getTitle().replace(" ", "%20"));
        } else {
            modelAndView.setViewName("/registrationTest");
        }


        return  modelAndView;
    }

    @RequestMapping(value = "/games/all", method = RequestMethod.GET)
    public ModelAndView getAllGames(){
        List<Game> games = gameService.getGamesTitles();
        System.out.println(games.size());
        System.out.println(games.get(1));
        modelAndView.addObject("games", games);
        modelAndView.setViewName("/listGame");
        return modelAndView;
    }

    @RequestMapping(value = "games/profile/{gameTitle}", method = RequestMethod.GET)
    public ModelAndView getProfileGame(@PathVariable("gameTitle") String gameTitle, HttpServletRequest request){

        modelAndView.clear();
        Game game = gameService.getGameByTitle(gameTitle);
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByName( principal.getName());

        if(gameService.isVoted(user.getId(), gameTitle))
            modelAndView.addObject("isVoted", "Zagłosowałeś już na tę grę");
        else
            modelAndView.addObject("isVoted", "Oceń grę!");

        String s = createAvatarString(game);

        modelAndView.addObject("img", s);
        modelAndView.addObject("title", game.getTitle());
        modelAndView.addObject("about", game.getAbout());
        modelAndView.addObject("developer", game.getDeveloper());
        modelAndView.addObject("releaseDate",game.getReleaseDate());
        modelAndView.addObject("rating", Math.round(game.getRating()));
        modelAndView.addObject("votes_amount", game.getVotesAmount());
        modelAndView.addObject("comment", new Comment());
        List<Comment> a = commentService.getMainComments(gameTitle);
        Collections.reverse(a);
        modelAndView.addObject("commentsList", a);
        modelAndView.addObject("commentsAmount", String.valueOf(commentService.getMainComments(gameTitle).size()));
//        if (reviewService.getReviewsPerGame(gameTitle).size() > 0 ) {
//            modelAndView.addObject("COSTAM", reviewService.getReviewsPerGame(gameTitle).get(0).getReviewTitle());
//            modelAndView.addObject("costam_id", reviewService.getReviewsPerGame(gameTitle).get(0).getId());
//            modelAndView.addObject("costamcontent", reviewService.getReviewsPerGame(gameTitle).get(0).getContent().substring(0,20));
//        }
        modelAndView.addObject("reviews",getThreeRevs(gameTitle));
        modelAndView.setViewName("/gameProfile");
        return modelAndView;
    }


    private List<Review> getThreeRevs(String gameTitle){
        List<Review> reviews = reviewService.getReviewsPerGame(gameTitle);
        List<Review> revs = new ArrayList<Review>();
        if (reviews.size() > 3) {
            revs = reviews.subList(0, 3);
        } else if (reviews.size() <= 0 ) {
            revs.add(new Review(0,"BRAK RECENZJI", "BRAK RECENZJI"));
        } else {
            revs = reviews;
        }

        for (Review rev : revs){
            if(rev.getContent().length() > 50)
                rev.setContent(rev.getContent().substring(0,50));
        }
        return revs;
    }


    private String createAvatarString(Game game) {
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
        return "data:image/png;base64,"+s;
    }

    @RequestMapping(value = "games/profile/{gameTitle}/addReview", method = RequestMethod.GET)
    public ModelAndView getAddReview(@PathVariable("gameTitle") String gameTitle){
        Game game = gameService.getGameByTitle(gameTitle);
        modelAndView.addObject("review", new Review());
        modelAndView.addObject("gameTitle", gameTitle);
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
        modelAndView.addObject("gameTitle", gameTitle);

        boolean hasErrors = false;
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("p", "B L A D ");
            hasErrors = true;
        }
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
    public ModelAndView addVote(@PathVariable("gameTitle") String gameTitle, @PathVariable("vote") Integer vote, HttpServletRequest request){
        Game game = gameService.getGameByTitle(gameTitle);
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByName( principal.getName());

        if(!gameService.isVoted(user.getId(), gameTitle)) {
            game.setVotesAmount(game.getVotesAmount() + 1);
            game.setVotesSum(game.getVotesSum() + vote);
            game.setRating(game.getVotesSum().doubleValue() / game.getVotesAmount().doubleValue());
            gameService.updateGameRating(game);
            gameService.addVoteMapping(vote,user.getId(),gameTitle);
            modelAndView.clear();
        }
          modelAndView.clear();
            modelAndView.setViewName("redirect:/games/profile/" + gameTitle.replace(" ", "%20"));
        return modelAndView;
    }


    @RequestMapping(value = "games/profile/{gameTitle}/addCom", method = RequestMethod.POST, params = "action=addComm")
    public ModelAndView addCommentToGame(@Valid Comment comment,@PathVariable("gameTitle") String gameTitle,BindingResult bindingResult, HttpServletRequest request){
        int gameId = gameService.getGameByTitle(gameTitle).getId();
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByName( principal.getName());

        comment.setKey_value(gameId);
        comment.setAuthor(user.getId());
        comment.setType("G");
        commentService.addComment(comment);

        modelAndView.clear();
        modelAndView.setViewName("redirect:/games/profile/"+gameTitle.replace(" ","%20"));
        return modelAndView;
    }


}
