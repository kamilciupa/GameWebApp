package com.gameweb.DAO;

import com.gameweb.model.Comment;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDAO {


        @Autowired
        JdbcTemplate jdbcTemplate;

        // Make Bean and autowire that
        Queries queries = new Queries();


        public void addComment(Comment comment){
            try{
                jdbcTemplate.update(queries.I_COMMENT,comment.getContent(),comment.getParent(),comment.getType(),comment.getKey_value(),comment.getAuthor());
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("BLAD");
            }
        }


}
