package com.gameweb.DAO;

import com.gameweb.model.Review;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReviewDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Make Bean and autowire that
    Queries queries = new Queries();


    public void addReviewToDB(Review review){
        try{
            jdbcTemplate.update(queries.I_REVIEW,review.getReviewTitle(), review.getContent(), review.getKey_value(), review.getParentId());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

  public List<Review> getReviewsPerGame(String gameTitle) {
    List<Review> a =
        jdbcTemplate.query(
            queries.S_GET_REVIEWS_PER_GAME,
            new RowMapper<Review>() {
              @Override
              public Review mapRow(ResultSet resultSet, int i) throws SQLException {
                Review e = new Review();
                  fillReviewData(resultSet, e);
                return e;
              }
            },
            gameTitle);
    return a;
  }

    private void fillReviewData(ResultSet resultSet, Review e) throws SQLException {
        e.setReviewTitle(resultSet.getString("title"));
        e.setContent(resultSet.getString("content"));
        e.setParentId(resultSet.getInt("author"));
        e.setKey_value(resultSet.getInt("key_value"));
        e.setId(resultSet.getInt("id"));
    }

    public Review getReviewById(Integer id) {
        Review review = new Review();
        try {
          review =   jdbcTemplate.queryForObject(queries.S_REVIEW_BY_ID, new RowMapper<Review>() {
                @Override
                public Review mapRow(ResultSet resultSet, int i) throws SQLException {
                    Review review = new Review();
                    review.setKey_value(resultSet.getInt("key_value"));
                    review.setParentId(resultSet.getInt("author"));
                    review.setContent(resultSet.getString("content"));
                    review.setReviewTitle(resultSet.getString("title"));
                    return review;
                }
            }, id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        review.setAuthorName((String) jdbcTemplate.queryForObject(
                queries.S_USERNAME_BY_ID, new Object[] { review.getParentId() }, String.class));

        review.setGameName((String) jdbcTemplate.queryForObject(
                queries.S_GAMETITLE_BY_ID, new Object[] { review.getKey_value() }, String.class));


        return  review;
    }


    public int getAmountPerGame(String game) {
        return jdbcTemplate.queryForObject(Queries.S_AMOUN_REVS_PER_GAME, new Object[] { game }, Integer.class);

    }

    public List<Review> getReviewsPerGamePerPage(String gameTitle, int pageid) {
        String sql="select * from reviews where key_value in (select id from games where title = ? ) order by title limit 10 offset "+ (pageid-1)*10;

        return jdbcTemplate.query(sql,new RowMapper<Review>(){
            public Review mapRow(ResultSet rs, int row) throws SQLException {
                Review e = new Review();
                fillReviewData(rs, e);
                return e;
            }
        },gameTitle);
    }



}
