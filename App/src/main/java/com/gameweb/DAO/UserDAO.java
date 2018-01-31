package com.gameweb.DAO;

import com.gameweb.model.User;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2017-12-13.
 */
@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Make Bean and autowire that
    Queries queries = new Queries();

    public void addUserToDB(User user){
        try {
            jdbcTemplate.update(queries.I_USER, user.getUsername(), user.getEmail(), user.getPassword());
            jdbcTemplate.update(queries.I_USER_ROLE, user.getUsername(), "ROLE_ADMIN");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Błąd w DAO");
        }
    }

    public List<User> getAllUsers() {
        try{
            List<User> users = new ArrayList<User>();
                    jdbcTemplate.query(queries.S_USERS, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet resultSet, int i) throws SQLException {
                    User user = new User();
                    user.setId(resultSet.getInt(1));
                    user.setUsername(resultSet.getString(2));
                    user.setEmail(resultSet.getString(3));
                    user.setPassword(resultSet.getString(4));
                    user.setAbout(resultSet.getString(5));
                    user.setAvatar(resultSet.getBytes(6));
                    users.add(user);
                    return user;
                }
            });
                    return users;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Nie można pobrać użytkowników ");
        }
        return null;
    }

    public User getUserByName(String username) {
        try{
           User users =
            jdbcTemplate.queryForObject(queries.S_USER_BY_NAME, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet resultSet, int i) throws SQLException {
                    User user = new User();
                    user.setId(resultSet.getInt(1));
                    user.setUsername(resultSet.getString(2));
                    user.setEmail(resultSet.getString(3));
                    user.setPassword(resultSet.getString(4));
                    user.setAbout(resultSet.getString(6));
                    user.setAvatar(resultSet.getBytes(5));
                    return user;
                }
            }, username);   return users; } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Nie można pobrać użytkowników ");
        }
         return new User();
    }

    public void updateAbout(User user) {
        jdbcTemplate.update(queries.U_USER_ABOUT, user.getAbout(), user.getUsername());
    }

    public void updateAvatar(User user){
        jdbcTemplate.update(queries.U_USER_AVATAR, user.getAvatar(), user.getUsername());
    }
}
