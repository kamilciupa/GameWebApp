package com.gameweb.DAO;

import com.gameweb.model.Game;
import com.gameweb.model.User;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class GameDAO {


        @Autowired
        JdbcTemplate jdbcTemplate;

        // Make Bean and autowire that
        Queries queries = new Queries();


        public void addGameToDB(Game game, Integer userID){
            try{
                jdbcTemplate.update(queries.I_GAME, game.getTitle(), game.getAbout(),game.getDeveloper(),game.getReleaseDate(),game.getCover(),userID);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Błąd w DAO");
            }
        }


        public List<Game> getGamesTitles(){
            List<Game> a = jdbcTemplate.query(queries.S_GET_GAMES_TITLES, new RowMapper<Game>() {
                @Override
                public Game mapRow(ResultSet rs, int rownumber) throws SQLException {
                    Game e = new Game();
                    e.setTitle(rs.getString("title"));
                    return e;
                }
            });
            return a;
        }

    public Game getUserByTitle(String title) {
            return  jdbcTemplate.queryForObject(queries.S_GAME_BY_TITLE, new RowMapper<Game>() {
                @Override
                public Game mapRow(ResultSet resultSet, int i) throws SQLException {
                    Game game = new Game();
                    game.setId(resultSet.getInt("id"));
                    game.setTitle(resultSet.getString("title"));
                    game.setAbout(resultSet.getString("about"));
                    game.setDeveloper(resultSet.getString("developer"));
                    game.setReleaseDate(resultSet.getDate("release_date"));
                    game.setCover(resultSet.getBytes("cover"));
                    return game;
                }
            },title);

    }
}
