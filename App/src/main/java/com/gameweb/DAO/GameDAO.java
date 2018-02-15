package com.gameweb.DAO;

import com.gameweb.model.Game;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class GameDAO {


        @Autowired
        JdbcTemplate jdbcTemplate;

        // Make Bean and autowire that
        Queries queries = new Queries();


        public void addGameToDB(Game game){
            try{
                //jdbcTemplate.update(queries.I_GAME)
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Błąd w DAO");
            }
        }



}
