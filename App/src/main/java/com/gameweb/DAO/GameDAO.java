package com.gameweb.DAO;

import com.gameweb.model.Game;
import com.gameweb.model.User;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.Principal;


@Repository
public class GameDAO {


        @Autowired
        JdbcTemplate jdbcTemplate;

        // Make Bean and autowire that
        Queries queries = new Queries();


        public void addGameToDB(Game game, Integer userID){
            try{
                jdbcTemplate.update(queries.I_GAME, game.getTitle(), game.getAbout(),userID);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Błąd w DAO");
            }
        }



}
