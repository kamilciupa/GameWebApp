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

  @Autowired JdbcTemplate jdbcTemplate;

  // Make Bean and autowire that
  Queries queries = new Queries();

  public void addGameToDB(Game game, Integer userID) {
    try {
      jdbcTemplate.update(
          queries.I_GAME,
          game.getTitle(),
          game.getAbout(),
          game.getDeveloper(),
//          game.getReleaseDate(),
          game.getCover(),
          userID);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Game> getGamesTitles() {
    List<Game> a =
        jdbcTemplate.query(
            queries.S_GET_GAMES_TITLES,
            new RowMapper<Game>() {
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
    try{
      Game game = jdbcTemplate.queryForObject(
              queries.S_GAME_BY_TITLE,
              new RowMapper<Game>() {
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
              },
              title);
    return game;
    }
    catch (Exception e){
      Game g = new Game();
      g.setId(-1);
      return  g;
    }
  }

  public void updateGameRating(Integer votesAmount, Integer votesSum, Double rating, String title) {
    try {
      jdbcTemplate.update(queries.U_GAME_RATING, votesAmount, votesSum, rating, title);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addVoteMapping(Integer vote, Integer user_id, String gameTitle) {
    try {
      jdbcTemplate.update(queries.I_USER_VOTE_MAP, user_id, gameTitle, vote);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Integer getVoteMapping(Integer userId, String gameTitle) {
    Integer vote = 0;
    try {
      vote =
          jdbcTemplate.queryForObject(
              queries.S_VOTE_MAPPING,
              new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet rs, int rownumber) throws SQLException {
                  return rs.getInt("vote");
                }
              },
              userId,
              gameTitle);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return vote;
  }

  public List<Game> getTopListGames() {
    List<Game> a =
        jdbcTemplate.query(
            queries.S_GAMES_ORDERED_BY_RATING,
            new RowMapper<Game>() {
              @Override
              public Game mapRow(ResultSet resultSet, int i) throws SQLException {
                Game e = new Game();
                fillGameData(resultSet, e);
                return e;
              }
            });
    return a;
  }

  public List<Game> getSearchedGames(String searchString) {
    List<Game> a =
        jdbcTemplate.query(
            queries.S_GAMES_LIKE,
            new RowMapper<Game>() {
              @Override
              public Game mapRow(ResultSet resultSet, int i) throws SQLException {
                Game e = new Game();
                fillGameData(resultSet, e);
                return e;
              }
            },
            searchString);

    return a;
  }

  private void fillGameData(ResultSet resultSet, Game e) throws SQLException {
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
  }

  public void updateCover(Game game) {
    jdbcTemplate.update(queries.U_GAME_COVER, game.getCover(), game.getTitle());
  }

  public void updateGameInfo(Game game) {
    jdbcTemplate.update(queries.U_GAME_ABOUT, game.getAbout(), game.getTitle());
    jdbcTemplate.update(queries.U_GAME_DEVELOPER, game.getDeveloper(), game.getTitle());
  }

  public int deleteGame(String name, String gameTitle) {
    int diff =
        jdbcTemplate.queryForObject(
            queries.S_GAME_MASTER,
            new RowMapper<Integer>() {
              @Override
              public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("diff");
              }
            },
            gameTitle,
            name);
    if (diff == 0) {
      jdbcTemplate.update(queries.D_GAME_VOTES, gameTitle);
      jdbcTemplate.update(queries.D_GAME_COM, gameTitle);
      jdbcTemplate.update(queries.D_GAME_REV, gameTitle);
      jdbcTemplate.update(queries.D_GAME, gameTitle, name);
    } else {
      return 1;
    }
    return 0;
  }

  public List<Game> getGameByPage(int pageid,int total){
    String sql="select * from games order by title limit 10 offset "+ (pageid-1)*10;
    return jdbcTemplate.query(sql,new RowMapper<Game>(){
      public Game mapRow(ResultSet rs, int row) throws SQLException {
        Game e = new Game();
        fillGameData(rs,e);
        return e;
      }
    });
  }

  public int getGamesAmount(){
    return jdbcTemplate.queryForObject(Queries.SELECT_COUNT_1_FROM_GAMES, new Object[] {}, Integer.class);
  }
}


