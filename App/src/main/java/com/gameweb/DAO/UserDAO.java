package com.gameweb.DAO;

import com.gameweb.model.User;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by Kamil on 2017-12-13.
 */
@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;


    Queries queries = new Queries();

    public void addUserToDB(User user){
        try {
        jdbcTemplate.update(queries.I_USER, user.getUsername(), user.getEmail(), user.getPassword());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Błąd w DAO");
        }


    }
}
