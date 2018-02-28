package com.gameweb.DAO;

import com.gameweb.model.Review;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

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
            System.out.println("BLAD");
        }
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



}
