package com.gameweb.DAO;

import com.gameweb.model.User;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

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
//        jdbcTemplate.update(queries.I_USER, user.getUsername(), user.getEmail(), user.getPassword());
            jdbcTemplate.update(queries.I_USER_W_AVATAR, user.getUsername(), user.getEmail(), user.getPassword(), user.getAvatar());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Błąd w DAO");
        }
    }

    public byte[] getAvatar(){
        byte[] a = jdbcTemplate.queryForObject(
                queries.S_AVATAR,
                (rs, rowNum) -> rs.getBytes(1));
        return a;
    }

}
