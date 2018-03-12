package com.gameweb.service;

import com.gameweb.DAO.GameDAO;
import com.gameweb.model.Game;
import com.gameweb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * Created by Kamil on 2017-11-29.
 */
@Service
public class GameService {

    @Autowired
    GameDAO gameDAO;

    @Autowired
    UserService userService;


    public ResponseEntity addGame(Game userInput, Integer userId){
        Game game = userInput;

        gameDAO.addGameToDB(game, userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    public Game getGameByTitle(String title){
        return gameDAO.getUserByTitle(title);
    }

    public List<Game> getGamesTitles(){ return  gameDAO.getGamesTitles();}

}
