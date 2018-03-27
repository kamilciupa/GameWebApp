package com.gameweb.utils;

/**
 * Created by Kamil on 2017-11-29.
 */

public class Queries
{

    static public final String S_USERS = "SELECT * FROM users";
    static public final String S_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";
    static public final String S_USERNAME_BY_ID = "SELECT username FROM users WHERE id = ?";
    static public final String S_GAMES = "SELECT * FROM games";
    static public final String S_GAME_BY_TITLE = "SELECT * FROM games WHERE title = ?";
    static public final String S_GAMETITLE_BY_ID = "SELECT title FROM games WHERE id =?";
    static public final String S_REVIEWS = "SELECT * FROM reviews";
    static public final String S_CATEGORIES = "SELECT * FROM categories";
    static public final String S_IS_VOTED = "SELECT vote FROM user_vote where game_id = ? and user_id = ?";

    static public final String S_GET_DEF_AVATAR = "SELECT photo FROM photo_def WHERE id = 1";

    static public final String S_AVATAR = "SELECT avatar FROM users where id = (SELECT MAX(ID) FROM USERS)";

    static public final String I_USER = "INSERT INTO users (username, email, password) VALUES (?,?,?)";
    static public final String I_USER_W_AVATAR = "INSERT INTO users (username, email, password,avatar) VALUES (?,?,?,?)";
    static public final String I_GAME = "INSERT INTO games (title,about, developer, release_date,cover,  masterid) VALUES (?,?,?,?,?,?)";
    static public final String I_REVIEW = "INSERT INTO reviews (title, content, key_value, author) VALUES (?,?,?,?)";
    static public final String I_USER_ROLE = "INSERT INTO user_roles (username, role) VALUES (?,?)";
    static public final String I_CATEGORY_GAME = "INSERT INTO categories_games (cat_id, game_id) values (?,?)";
    static public final String I_USER_VOTE_MAP = "INSERT INTO user_vote (user_id, game_id, vote)\n" +
            " values (?,(select id from games where title = ?),?);";


    static public final String U_USER_ABOUT = "UPDATE users set about = ? where username = ?";
    static public final String U_USER_AVATAR = "UPDATE users set avatar = ? where username = ?";

    static public final String U_GAME_ABOUT = "UPDATE games set about = ? where id = ?";
    static public final String U_GAME_COVER = "UPDATE games set cover = ? where id = ?";

    public static final String S_REVIEW_BY_ID = "SELECT * FROM reviews WHERE id = ?";
    public static final String S_GET_GAMES_TITLES = "SELECT title FROM games";
    public static final String U_GAME_RATING = "UPDATE games SET votes_amount = ? , votes_sum = ?, rating = ? where title = ?";
    public static final String I_COMMENT = "INSERT INTO commments (content, parent, type, key_value, author) values (?,?,?,?,?)";
    public static final String S_GET_REVIEWS_PER_GAME = "SELECT id, title, content, key_value, author FROM reviews where key_value = (SELECT id FROM games where title = ?) ";
    public static final String S_VOTE_MAPPING = "SELECT count(1) vote FROM user_vote where user_id = ? and game_id = (select id from games where title = ?)";
}
