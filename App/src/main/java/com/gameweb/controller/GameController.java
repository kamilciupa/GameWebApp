package com.gameweb.controller;

import com.gameweb.model.*;
import com.gameweb.service.*;
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
import java.util.stream.Collectors;

@RestController
public class GameController {

  @Autowired GameService gameService;
  @Autowired UserService userService;
  @Autowired ReviewService reviewService;
  @Autowired CommentService commentService;
  @Autowired
  CategoryService categoryService;

  ModelAndView modelAndView = new ModelAndView();

  private static void setupSearchBar(ModelAndView modelAndView) {
    Game gg = new Game();
    modelAndView.addObject("searchS", gg);
  }

  private void getUsernameForModel(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());
    modelAndView.addObject("username", user.getUsername());
  }

  private String createAvatarString(Game game) {
    String s = "";
    byte[] bytes;
    try {
      if (game.getCover() == null) {
        bytes = userService.getDefaultAvatar();
      } else {
        bytes = game.getCover();
      }
      org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
      s = encoder.encodeToString(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "data:image/png;base64," + s;
  }

  @RequestMapping(value = "/games/addGame", method = RequestMethod.GET)
  public ModelAndView addGameShow(HttpServletRequest request) {
    getUsernameForModel(request);
    setupSearchBar(modelAndView);
    modelAndView.addObject("game", new Game());
    List<Category> cat = categoryService.getCategories();
    modelAndView.addObject("cats", cat);
    modelAndView.setViewName("/addGame");
    return modelAndView;
  }

  @RequestMapping(value = "/games/addGame", method = RequestMethod.POST)
  public ModelAndView registration(
      @Valid Game game,
      @RequestPart(value = "file", required = false) MultipartFile file,
      BindingResult bindingResult,
      HttpServletRequest request)
      throws IOException {
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());
    modelAndView.addObject("game", new Game());
    boolean hasErrors = false;
    if (bindingResult.hasErrors()) {
      modelAndView.addObject("p", "B L A D ");
      hasErrors = true;
    }

    //        TODO walidacje

    if (!gameService.isGameTitleFree(game.getTitle())) {
      modelAndView.addObject("p", "B L A D ");
      hasErrors = false;
    }

    if (!hasErrors) {
      game.setCover(file.getBytes());
      gameService.addGame(game, user.getId());
      modelAndView.setViewName("/profile");
      modelAndView.clear();
      modelAndView.setViewName("redirect:/games/profile/" + game.getTitle().replace(" ", "%20"));
    } else {
      modelAndView.setViewName("/registrationTest");
    }

    return modelAndView;
  }

//  @RequestMapping(value = "/games/all", method = RequestMethod.GET)
//  public ModelAndView getAllGames(HttpServletRequest request) {
//    getUsernameForModel(request);
//    List<Game> games = gameService.getGamesTitles();
//    setupSearchBar(modelAndView);
////    List<Game> gam = games.stream().sorted().collect(Collectors.toList());
//    modelAndView.addObject("games", games);
//    modelAndView.setViewName("/listGame");
//    return modelAndView;
//  }

  @RequestMapping(value="/games/all/{pageid}")
  public ModelAndView edit(@PathVariable int pageid, HttpServletRequest request) {
    int total=gameService.getGamesAmount();
    total = (total / 10) +  1;
    modelAndView.clear();
    getUsernameForModel(request);
    setupSearchBar(modelAndView);

    List<Game> games = gameService.getGamesByPage(pageid);

    for(Game g : games){
      g.setAbout(g.getAbout().substring (0,(g.getAbout().length() < 80 ? g.getAbout().length() : 80 )));
    }

    modelAndView.addObject("prev_pageid", (pageid == 1 ? 1 : pageid-1 ));
    modelAndView.addObject("next_pageid",(pageid == total ? total : pageid+1));

    modelAndView.addObject("games", games);
    modelAndView.setViewName("listGame");
    return modelAndView;
//    return new ModelAndView("listGame","games",games);
  }

  @RequestMapping(value = "/games/top", method = RequestMethod.GET)
  public ModelAndView getAllGamesToplist(HttpServletRequest request) {
    getUsernameForModel(request);
    setupSearchBar(modelAndView);
    List<Game> games = gameService.getGamesOrderedTop();
    List<String> gTitles = new ArrayList<>();
    for (Game g : games) {
      gTitles.add(g.getTitle());
    }
//    Collections.reverse(gTitles);
    for(Game g : games){
      g.setAbout(g.getAbout().substring (0,(g.getAbout().length() < 80 ? g.getAbout().length() : 80 )));
    }
    modelAndView.addObject("games", games);
    modelAndView.setViewName("/toplist");
    return modelAndView;
  }

  @RequestMapping(value = "games/profile/{gameTitle}", method = RequestMethod.GET)
  public ModelAndView getProfileGame(
      @PathVariable("gameTitle") String gameTitle, HttpServletRequest request) {

    modelAndView.clear();
    setupSearchBar(modelAndView);
    getUsernameForModel(request);
    Game game = gameService.getGameByTitle(gameTitle);
    if(game.getId() == -1){
      modelAndView.clear();
      modelAndView.setViewName("redirect:/error");
      return modelAndView;
    }
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());
    user.setUserGames(userService.getUserGames(user));

    List<String> ggg = new ArrayList<String>();
    List<String> gg2 = new ArrayList<String>();
    for (Game a : user.getUserGames()) {
      ggg.add(a.getTitle());
    }
    if (ggg.contains(gameTitle)) {
      gg2.add("");
    }

    modelAndView.addObject("if", gg2);

    if (gameService.isVoted(user.getId(), gameTitle))
      modelAndView.addObject("isVoted", "Zagłosowałeś już na tę grę");
    else modelAndView.addObject("isVoted", "Oceń grę!");

    String s = createAvatarString(game);

    modelAndView.addObject("img", s);
    modelAndView.addObject("title", game.getTitle());
    modelAndView.addObject("about", game.getAbout());
    modelAndView.addObject("developer", game.getDeveloper());
    modelAndView.addObject("releaseDate", game.getReleaseDate());
    modelAndView.addObject("rating", Math.round(game.getRating()));
    modelAndView.addObject("votes_amount", game.getVotesAmount());
    modelAndView.addObject("cat_name", game.getCat_name());
    modelAndView.addObject("comment", new Comment());
    List<Comment> a = commentService.getMainComments(gameTitle);
    Collections.reverse(a);
    modelAndView.addObject("commentsList", a);
    modelAndView.addObject(
        "commentsAmount", String.valueOf(commentService.getMainComments(gameTitle).size()));
    modelAndView.addObject("reviews", getThreeRevs(gameTitle));
    modelAndView.setViewName("/gameProfile");
    return modelAndView;
  }

  private List<Review> getThreeRevs(String gameTitle) {
    List<Review> reviews = reviewService.getReviewsPerGame(gameTitle);
    List<Review> revs = new ArrayList<Review>();
    if (reviews.size() > 3) {
      revs = reviews.subList(0, 3);
    } else {
      revs = reviews;
    }

    for (Review rev : revs) {
      if (rev.getContent().length() > 50) rev.setContent(rev.getContent().substring(0, 50));
    }
    return revs;
  }

  @RequestMapping(value = "games/profile/{gameTitle}/addReview", method = RequestMethod.GET)
  public ModelAndView getAddReview(
      @PathVariable("gameTitle") String gameTitle, HttpServletRequest request) {
    getUsernameForModel(request);
    Game game = gameService.getGameByTitle(gameTitle);
    setupSearchBar(modelAndView);
    modelAndView.addObject("review", new Review());
    modelAndView.addObject("gameTitle", gameTitle);
    modelAndView.addObject("title", game.getTitle());
    modelAndView.setViewName("/addReview");
    return modelAndView;
  }

  @RequestMapping(value = "/games/profile/{gameTitle}/addReview", method = RequestMethod.POST)
  public ModelAndView addReviewPost(
      @Valid Review review,
      @PathVariable("gameTitle") String gameTitle,
      BindingResult bindingResult,
      HttpServletRequest request)
      throws IOException {

    setupSearchBar(modelAndView);
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());
    Game game = gameService.getGameByTitle(gameTitle);
    modelAndView.addObject("review", new Review());
    modelAndView.addObject("gameTitle", gameTitle);

    boolean hasErrors = false;
    if (bindingResult.hasErrors()) {
      modelAndView.addObject("p", "B L A D ");
      hasErrors = true;
    }
    if (!hasErrors) {
      review.setParentId(user.getId());
      review.setKey_value(game.getId());
      reviewService.addReview(review);
      modelAndView.setViewName("/addReview");
    } else {
      modelAndView.setViewName("/registrationTest");
    }
    return modelAndView;
  }

  // TODO Przenieść to do kontrolera od recenzji -> Stworzyć kontroler od recenzji

  @RequestMapping(value = "/reviews/{id}", method = RequestMethod.GET)
  public ModelAndView getWholeReview(
      @PathVariable("id") Integer reviewId, HttpServletRequest request) {
    getUsernameForModel(request);
    setupSearchBar(modelAndView);
    Review review = reviewService.getReviewById(reviewId);

    modelAndView.addObject("content", review.getContent());
    modelAndView.addObject("title", review.getReviewTitle());
    modelAndView.addObject("author", review.getAuthorName());
    modelAndView.addObject("gameTitle", review.getGameName());

    modelAndView.setViewName("/wholeReview");
    return modelAndView;
  }

  @RequestMapping(value="games/profile/{gameTitle}/reviews/{pageid}")
  public ModelAndView getReviews(@PathVariable("gameTitle") String gameTitle,@PathVariable("pageid") int pageid, HttpServletRequest request) {
    int total=reviewService.getReviewsAmountPerGame(gameTitle);
    total = (total / 10) +  1;
    modelAndView.clear();
    getUsernameForModel(request);
    setupSearchBar(modelAndView);

    List<Review> reviews = reviewService.getReviewsByPagePerGame(gameTitle, pageid);

    modelAndView.addObject("gameTitle", gameTitle);
    modelAndView.addObject("prev_pageid", (pageid == 1 ? 1 : pageid-1 ));
    modelAndView.addObject("next_pageid",(pageid == total ? total : pageid+1));

    modelAndView.addObject("reviews", reviews);
    modelAndView.setViewName("listReviews");
    return modelAndView;
  }





  @RequestMapping(value = "/games/profile/{gameTitle}/vote/{vote}", method = RequestMethod.GET)
  public ModelAndView addVote(
      @PathVariable("gameTitle") String gameTitle,
      @PathVariable("vote") Integer vote,
      HttpServletRequest request) {
    Game game = gameService.getGameByTitle(gameTitle);
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());

    if (!gameService.isVoted(user.getId(), gameTitle)) {
      game.setVotesAmount(game.getVotesAmount() + 1);
      game.setVotesSum(game.getVotesSum() + vote);
      game.setRating(game.getVotesSum().doubleValue() / game.getVotesAmount().doubleValue());
      gameService.updateGameRating(game);
      gameService.addVoteMapping(vote, user.getId(), gameTitle);
      modelAndView.clear();
    }
    modelAndView.clear();
    modelAndView.setViewName("redirect:/games/profile/" + gameTitle.replace(" ", "%20"));
    return modelAndView;
  }

  @RequestMapping(
    value = "games/profile/{gameTitle}/addCom",
    method = RequestMethod.POST,
    params = "action=addComm"
  )
  public ModelAndView addCommentToGame(
      @Valid Comment comment,
      @PathVariable("gameTitle") String gameTitle,
      BindingResult bindingResult,
      HttpServletRequest request) {
    int gameId = gameService.getGameByTitle(gameTitle).getId();
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());

    comment.setKey_value(gameId);
    comment.setAuthor(user.getId());
    comment.setType("G");
    commentService.addComment(comment);

    modelAndView.clear();
    modelAndView.setViewName("redirect:/games/profile/" + gameTitle.replace(" ", "%20"));
    return modelAndView;
  }

  @RequestMapping(value = "/search")
  public ModelAndView search(@Valid Game g) {
    modelAndView.addObject("searchS", g);
    modelAndView.clear();
    modelAndView.setViewName("redirect:/games/search/" + g.getTitle());
    return modelAndView;
  }

  @RequestMapping(value = "/games/search/{searchString}", method = RequestMethod.GET)
  public ModelAndView getSearched(
      @PathVariable("searchString") String searchString, HttpServletRequest request) {
    modelAndView.clear();
    getUsernameForModel(request);
    setupSearchBar(modelAndView);
    List<Game> listGame = gameService.getSearchedGames(searchString);

    if (listGame.isEmpty()) {
      Game g = new Game();
      g.setTitle("Nie znaleziono gry");
      listGame.add(g);
    }
    modelAndView.addObject("result", listGame);
    modelAndView.setViewName("searchGames");
    return modelAndView;
  }

  @RequestMapping(value = "/games/profile/{gameTitle}/settings")
  public ModelAndView changeGameInfo(
      @PathVariable("gameTitle") String gameTitle, HttpServletRequest request) {
    modelAndView.clear();
    getUsernameForModel(request);
    setupSearchBar(modelAndView);
    modelAndView.addObject("gameTitle", gameTitle);
    Game game = gameService.getGameByTitle(gameTitle);
    modelAndView.addObject("game", game);
    String s = createAvatarString(game);
    modelAndView.addObject("img", s);
    modelAndView.setViewName("changeGameInfo");
    return modelAndView;
  }

  @RequestMapping(value = "/games/profile/{gameTitle}/settings", method = RequestMethod.POST)
  public ModelAndView changeGameInfo(
      @PathVariable("gameTitle") String gameTitle, @Valid Game game) {
    // todo
    game.setTitle(gameTitle);
    gameService.updateGameInfo(game);
    modelAndView.clear();
    modelAndView.setViewName(
        "redirect:/games/profile/" + gameTitle.replace(" ", "%20") + "/settings");
    return modelAndView;
  }

  @RequestMapping(value = "/uploadCover/{gameTitle}", method = RequestMethod.POST)
  public ModelAndView updateCover(
      @RequestParam("file") MultipartFile multipartFile,
      @PathVariable("gameTitle") String gameTitle) {
    String s = "";
    try {

      byte[] bytes = multipartFile.getBytes();
      Game game = gameService.getGameByTitle(gameTitle);
      game.setCover(bytes);
      gameService.updateCover(game);

    } catch (IOException e) {
      e.printStackTrace();
    }
    modelAndView.clear();
    modelAndView.setViewName(
        "redirect:/games/profile/" + gameTitle.replace(" ", "%20") + "/settings");
    return modelAndView;
  }

  @RequestMapping(value = "/games/profile/{gameTitle}/settings/delete", method = RequestMethod.POST)
  public ModelAndView deleteGame(
      @PathVariable("gameTitle") String gameTitle, HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    int s = gameService.deleteGame(principal.getName(), gameTitle);
    modelAndView.clear();
    if (s == 1) {
      modelAndView.setViewName(
          "redirect:/games/profile/" + gameTitle.replace(" ", "%20") + "/settings");
    } else {
      modelAndView.setViewName("redirect:/games/all");
    }
    return modelAndView;
  }


}
