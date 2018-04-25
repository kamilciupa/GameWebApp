package com.gameweb.controller;

import com.gameweb.model.Game;
import com.gameweb.model.User;
import com.gameweb.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.security.Principal;

@RestController
public class UserController {

  @Autowired UserService userService;

  ModelAndView modelAndView = new ModelAndView();

  private static void setupSearchForm(ModelAndView modelAndView) {
    Game gg = new Game();
    modelAndView.addObject("searchS", gg);
  }

  private static void handleError(ModelAndView modelAndView, Exception e, String endpoint) {
    modelAndView.clear();
    modelAndView.setViewName("redirect:/errorPage");
    System.out.println(endpoint + " ERROR:) " + e.getMessage());
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView getMainUnlogged() {
    modelAndView.setViewName("/mainUnlogged");
    return modelAndView;
  }

  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public ModelAndView getMainLogged(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());
    setupSearchForm(modelAndView);
    modelAndView.addObject("username", user.getUsername());
    modelAndView.clear();
    modelAndView.setViewName("redirect:/profile/" + user.getUsername());
    return modelAndView;
  }

  @RequestMapping(value = "/registration", method = RequestMethod.GET)
  public ModelAndView registrationGet() {
    modelAndView.addObject("user", new User());
    modelAndView.setViewName("/registrationTest");
    return modelAndView;
  }

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  public ModelAndView registration(@Valid User user, BindingResult bindingResult) {
    modelAndView.addObject("user", new User());
    boolean hasErrors = false;
    if (bindingResult.hasErrors()) {
      hasErrors = true;
    }
    if (!userService.isUsernameFree(user.getUsername())) {
      modelAndView.addObject("p", "Nazwa użytkownika jest zajęta");
      hasErrors = true;
    }
    if (!userService.isEmailFree(user.getEmail())) {
      modelAndView.addObject("p", "Email jest zajęty");
      hasErrors = true;
    }
    if (!hasErrors) {
      modelAndView.addObject("p", "Sukces");
      userService.registration(user);
      modelAndView.setViewName("/loginTestJsp");
    } else {
      modelAndView.setViewName("/registrationTest");
    }

    return modelAndView;
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public ModelAndView getLoginPage() {
    return checkAuthAndRedirect("index", "loginTestJsp");
  }

  private ModelAndView checkAuthAndRedirect(String ok, String wrong) {
    if (SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        && !(SecurityContextHolder.getContext().getAuthentication()
            instanceof AnonymousAuthenticationToken)) {
      modelAndView.clear();
      modelAndView.setViewName("redirect:/" + ok);
    } else modelAndView.setViewName("/" + wrong);
    return modelAndView;
  }

  @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
  public ModelAndView uploadPost(@RequestParam("file") MultipartFile uploadfile) {
    modelAndView.addObject("file", uploadfile);
    String s = "";
    try {
      byte[] bytes = uploadfile.getBytes();
      org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
      s = encoder.encodeToString(bytes);
    } catch (IOException e) {
      handleError(modelAndView, e, "/uploadFile");
      return modelAndView;
    }
    modelAndView.addObject("img", "data:image/png;base64," + s);
    modelAndView.setViewName("/settings");
    return modelAndView;
  }

  @RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
  public ModelAndView getProfile(@PathVariable("username") String username) {
    modelAndView.clear();
    User user = userService.getUserByName(username);
    if(user.getId() == -1){
      modelAndView.setViewName("redirect:/index");
      return modelAndView;
    }
    user.setUserGames(userService.getUserGames(user));
    setupSearchForm(modelAndView);
    String s = "";
    byte[] bytes;
    try {
      if (user.getAvatar() == null) bytes = userService.getDefaultAvatar();
      else bytes = user.getAvatar();
      org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
      s = encoder.encodeToString(bytes);
    } catch (Exception e) {
      handleError(modelAndView, e, "/profile/{username}");
      return modelAndView;
    }
    modelAndView.addObject("img", "data:image/png;base64," + s);
    modelAndView.addObject("username", user.getUsername());
    modelAndView.addObject("email", user.getEmail());
    modelAndView.addObject("about", user.getAbout());
    modelAndView.addObject("games", user.getUserGames());
    modelAndView.setViewName("/profile");
    return modelAndView;
  }

  @RequestMapping(value = "/settings", method = RequestMethod.GET)
  public ModelAndView getSettings(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());
    setupSearchForm(modelAndView);
    String s = "";
    try {
      byte[] bytes = user.getAvatar();
      org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
      s = encoder.encodeToString(bytes);

    } catch (Exception e) {
      handleError(modelAndView, e, "/settings");
      return modelAndView;
    }
    modelAndView.addObject("user", user);
    modelAndView.addObject("img", "data:image/png;base64," + s);
    modelAndView.addObject("username", user.getUsername());
    modelAndView.addObject("email", user.getEmail());
    modelAndView.addObject("about", user.getAbout());
    modelAndView.setViewName("/changeUserInfo");
    return modelAndView;
  }

  @RequestMapping(value = "/settings", method = RequestMethod.POST)
  public ModelAndView postSettings(
      @Valid User user, HttpServletRequest request, BindingResult bindingResult) {
    modelAndView.addObject("user", new User());
    Principal principal = request.getUserPrincipal();
    User userOld = userService.getUserByName(principal.getName());
    userOld.setAbout(user.getAbout());
    userOld.setEmail(user.getEmail());
    if (!bindingResult.hasErrors()) {
      userService.updateUserInfo(userOld);
    }
    modelAndView.addObject("username", user.getUsername());
    modelAndView.addObject("about", user.getAbout());
    modelAndView.clear();
    modelAndView.setViewName("redirect:/profile/" + userOld.getUsername());
    return modelAndView;
  }

  @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
  public ModelAndView updateAvatar(
      @RequestParam("file") MultipartFile uploadfile, HttpServletRequest request) {
    try {
      byte[] bytes = uploadfile.getBytes();
      Principal principal = request.getUserPrincipal();
      User userOld = userService.getUserByName(principal.getName());
      userOld.setAvatar(bytes);
      userService.updateAvatar(userOld);
    } catch (IOException e) {
      handleError(modelAndView, e, "/uploadAvatar");
      return modelAndView;
    }
    modelAndView.clear();
    modelAndView.setViewName("redirect:/settings");
    return modelAndView;
  }

  @RequestMapping(value = "/errorPage", method = RequestMethod.GET)
  public ModelAndView showErrorPage(HttpServletRequest request) {
    getUsernameForModel(request);
    setupSearchForm(modelAndView);
    modelAndView.setViewName("errorPage");
    return modelAndView;
  }


  private void getUsernameForModel(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    User user = userService.getUserByName(principal.getName());
    modelAndView.addObject("username", user.getUsername());
  }
}
