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
                    game.setRating(resultSet.getDouble("rating"));
                    game.setVotesSum(resultSet.getInt("votes_sum"));
                    game.setVotesAmount(resultSet.getInt("votes_amount"));
                    return game;
                }
            },title);

    }

    public void updateGameRating(Integer votesAmount, Integer votesSum, Double rating, String title) {
            try {
                jdbcTemplate.update(queries.U_GAME_RATING, votesAmount, votesSum, rating, title);
            } catch (Exception e){
                e.printStackTrace();
            }
    }

    public void addVoteMapping(Integer vote, Integer user_id, String gameTitle) {
            try {
                    jdbcTemplate.update(queries.I_USER_VOTE_MAP, user_id, gameTitle , vote );
                } catch (Exception e ){
                e.printStackTrace();
            }
    }

    public Integer getVoteMapping(Integer userId, String gameTitle){
        Integer vote = 0;
            try {
                vote = jdbcTemplate.queryForObject(queries.S_VOTE_MAPPING, new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet rs, int rownumber) throws SQLException {
                        //TODO Usunąc println
                        System.out.println("To mamy z DAO" + rs.getInt("vote"));
                        return rs.getInt("vote");
                    }
                },userId,gameTitle);
            } catch (Exception e){
                e.printStackTrace();
            }

        return vote;
    }


    public List<Game> getTopListGames(){
        List<Game> a = jdbcTemplate.query(queries.S_GAMES_ORDERED_BY_RATING, new RowMapper<Game>() {
            @Override
            public Game mapRow(ResultSet resultSet, int i) throws SQLException {
                Game e = new Game();
                e.setVotesAmount(resultSet.getInt("votes_amount"));
                e.setVotesSum(resultSet.getInt("votes_sum"));
                e.setRating(resultSet.getDouble("rating"));
                e.setTitle(resultSet.getString("title"));
                e.setId(resultSet.getInt("id"));
                e.setCover(resultSet.getBytes("cover"));
                e.setMasterId(resultSet.getInt("masterid"));
                e.setAbout(resultSet.getString("about"));
                e.setDeveloper(resultSet.getString("developer"));
                e.setReleaseDate(resultSet.getDate("release_date"));
                return e;
            }
        });
        return a;
    }

}
