package com.gameweb.service;

import com.gameweb.DAO.ReviewDAO;
import com.gameweb.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ReviewDAO reviewDAO;

    public ResponseEntity addReview(Review review) {
        reviewDAO.addReviewToDB(review);
        return new ResponseEntity(HttpStatus.OK);
    }


    public Review getReviewById(Integer reviewId) {
        return reviewDAO.getReviewById(reviewId);
    }

    public List<Review> getReviewsPerGame(String gameTitle) { return  reviewDAO.getReviewsPerGame(gameTitle);}

    public int getReviewsAmountPerGame(String game) {
        return reviewDAO.getAmountPerGame(game);
    }

    public List<Review> getReviewsByPagePerGame(String gameTitle, int pageid) {
        return reviewDAO.getReviewsPerGamePerPage(gameTitle, pageid);

    }
}
