package com.gameweb.DAO;

import com.gameweb.model.Category;
import com.gameweb.model.Game;
import com.gameweb.model.SortParams;
import com.gameweb.model.User;
import com.gameweb.service.UserService;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class GameDAO {

  @Autowired JdbcTemplate jdbcTemplate;
@Autowired UserService userService;
@Autowired CategoryDAO categoryDAO;
  // Make Bean and autowire that
  Queries queries = new Queries();

  public void addGameToDB(Game game, Integer userID) {
    try {
      jdbcTemplate.update(
          queries.I_GAME,
          game.getTitle(),
          game.getAbout(),
          game.getDeveloper(),
          game.getReleaseDate(),
          game.getCover(),
          userID);
      jdbcTemplate.update(queries.I_VOTE_INIT, game.getTitle());
      jdbcTemplate.update(queries.I_CAT_GAME, game.getCat_id(), game.getTitle());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addCategoryToGame(Game game, int c){
    jdbcTemplate.update(queries.I_CATEGORY_GAME, c,game.getId() );
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
                  fillCategory(game);
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
                fillCategory(e);
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
                fillCategory(e);
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
    e.setCoverByteString("data:image/png;base64," + getCoverString(e.getCover()));
  }

  private void fillCategory(Game e){

    Category c = categoryDAO.getCategoryByGame(e.getId());
    e.setCat_id(c.getId());
    e.setCat_name(c.getName());
    System.out.println(e.getCat_name());
  }


  private String getCoverString(byte[] avatar) {
    String s = "";
    byte[] bytes;
    try {
      if (avatar == null) {
        bytes = userService.getDefaultAvatar();
      } else {
        bytes = avatar;
      }
      org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
      s = encoder.encodeToString(bytes);
    } catch (Exception e) {

    }
    return s;
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



  public int getGamesAmount(){
    return jdbcTemplate.queryForObject(Queries.SELECT_COUNT_1_FROM_GAMES, new Object[] {}, Integer.class);
  }

  public int getGamesAmountSort(double fromRat, double toRat, Date fromDat, Date toDat, int tag, int sort) {
    String mainQuery = "select count(1) from games where rating between " + fromRat+" and " + toRat + " or rating is null" ;
    return jdbcTemplate.queryForObject(mainQuery, new Object[] {}, Integer.class);
  }

  public List<Game> getGameByPage(int pageid,int total){
    String sql="select * from games order by title limit 10 offset "+ (pageid-1)*10;
    return jdbcTemplate.query(sql,new RowMapper<Game>(){
      public Game mapRow(ResultSet rs, int row) throws SQLException {
        Game e = new Game();
        fillGameData(rs,e);
        fillCategory(e);
        return e;
      }
    });
  }

  public List<Game> getGamesByPageSort(int pageid, int total, SortParams sortParams) {
    String order = "title asc";
    switch(sortParams.getSort()){
      case 1:
        order = "title asc";
        break;
      case 2:
        order = "title desc";
        break;
      case 3:
        order = "rating asc";
        break;
      case 4:
        order = "rating desc";
        break;
      case 5:
        order = "release_date asc";
        break;
      case 6:
        order = "release_date desc";
        break;
    }

    String whereClauseCat = "and id in (select game_id from categories_games where cat_id = "+ sortParams.getTag()+ " )";
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
    String whereClauseDate = "and release_date between to_date('" + s.format(sortParams.getFromDat()) + "','YYYY-MM-DD') and to_date('" + s.format(sortParams.getToDat())+ "','YYYY-MM-DD') ";
    String whereClause = "where (rating between " + sortParams.getFromRat() + " and " + sortParams.getToRat() + " or rating is null) " + whereClauseDate ;

    String sql="select * from games " + whereClause+ " order by " + order + " limit 10 offset "+ (pageid-1)*10;
if (sortParams.getTag() != 999 ){
  sql = "select * from games " + whereClause+whereClauseCat+ " order by " + order + " limit 10 offset "+ (pageid-1)*10;
}

System.out.println(sql);
    return jdbcTemplate.query(sql,new RowMapper<Game>(){
      public Game mapRow(ResultSet rs, int row) throws SQLException {
        Game e = new Game();
        fillGameData(rs,e);
        fillCategory(e);
        return e;
      }
    });
  }
}


