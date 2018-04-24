package com.gameweb.DAO;

import ch.qos.logback.core.LogbackException;
import com.gameweb.model.Comment;
import com.gameweb.service.UserService;
import com.gameweb.utils.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

@Repository
public class CommentDAO {

  @Autowired JdbcTemplate jdbcTemplate;
  @Autowired UserService userService;

  Queries queries = new Queries();
  String className = "CommentDAO";

    private void printMyException(String className, String methodName, Exception e) {
        System.out.println(
                MessageFormat.format(
                        "1) {0}\n2) {1}\n3) {2}\n4) {3}",
                        className, methodName, e.getMessage(), e.getClass().getCanonicalName()));
    }


  public void addComment(Comment comment) {
    try {
      jdbcTemplate.update(
          queries.I_COMMENT,
          comment.getContent(),
          comment.getParent(),
          comment.getType(),
          comment.getKey_value(),
          comment.getAuthor());
    } catch (Exception e) {
      printMyException(className, "addComment", e);
    }
  }


  public List<Comment> getCommentsForGame(String gameTitle) {
    List<Comment> a =
        jdbcTemplate.query(
            queries.S_GET_COMMS,
            new RowMapper<Comment>() {
              @Override
              public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
                Comment c = new Comment();
                c = setCommentFromQuery(resultSet, c);
                return c;
              }
            },
            gameTitle);
    return a;
  }

  public List<Comment> getChildComment(int parent) {
    List<Comment> a =
        jdbcTemplate.query(
            queries.S_GET_CHILD_COMMS,
            new RowMapper<Comment>() {
              @Override
              public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
                Comment c = new Comment();
                c = setCommentFromQuery(resultSet, c);
                return c;
              }
            },
            parent);
    return a;
  }

  private Comment setCommentFromQuery(ResultSet resultSet, Comment c) throws SQLException {
    c.setContent(resultSet.getString("content"));
    c.setType(resultSet.getString("type"));
    c.setKey_value(resultSet.getInt("key_value"));
    c.setAuthor(resultSet.getInt("author"));
    c.setId(resultSet.getInt("id"));
    c.setParent(resultSet.getInt("parent"));
    c.setAuthor_name(resultSet.getString("username"));
    c.setAuthor_avatar("data:image/png;base64," + getAvatarString(resultSet.getBytes("avatar")));
    return c;
  }

  private String getAvatarString(byte[] avatar) {
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
        printMyException(className, "getAvatarString", e);
    }
    return s;
  }
}
