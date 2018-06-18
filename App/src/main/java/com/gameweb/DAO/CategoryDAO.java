package com.gameweb.DAO;

import com.gameweb.model.Category;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Queries queries = new Queries();


    public Category getCategoryByGame(int gameid){
        System.out.println(gameid);
        Category c = jdbcTemplate.queryForObject(queries.S_CAT_BY_GAME, new RowMapper<Category>(){
            @Override
            public Category mapRow(ResultSet rs, int i) throws SQLException {
                Category e = new Category();
                    e.setId(rs.getInt("id"));
                    e.setName(rs.getString("name"));

                return e;
            }
        },gameid);

        return c;
    }

    public List<Category> getCategories() {
        List<Category> a =
                jdbcTemplate.query(
                        queries.S_CATEGORIES,
                        new RowMapper<Category>() {
                            @Override
                            public Category mapRow(ResultSet rs, int rownumber) throws SQLException {
                                Category e = new Category();
                               e.setId(rs.getInt("id"));
                               e.setName(rs.getString("name"));
                                return e;
                            }
                        });
        return a;
    }

    public Category getCatByName(String name){
        Category c = jdbcTemplate.queryForObject(queries.S_CAT_BY_NAME, new RowMapper<Category>(){
            @Override
            public Category mapRow(ResultSet rs, int i) throws SQLException {
                Category e = new Category();
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("name"));
                return e;
            }
        },name);
        return c;
    }

}
