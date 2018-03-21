package com.gameweb.service;

import com.gameweb.DAO.CommentDAO;
import com.gameweb.DAO.ReviewDAO;
import com.gameweb.model.Comment;
import com.gameweb.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentService {


    @Autowired
    CommentDAO commentDAO;

    public ResponseEntity addComment(Comment comment) {
        commentDAO.addComment(comment);
        return new ResponseEntity(HttpStatus.OK);
    }
}
