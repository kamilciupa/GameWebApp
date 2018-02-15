package com.gameweb.service;

import com.gameweb.DAO.GameDAO;
import com.gameweb.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by Kamil on 2017-11-29.
 */
@Service
public class GameService {

    @Autowired
    GameDAO gameDAO;



    public ResponseEntity addGame(Game userInput){
        Game game = userInput;

        gameDAO.addGameToDB(game);
        return new ResponseEntity(HttpStatus.OK);
    }



}
