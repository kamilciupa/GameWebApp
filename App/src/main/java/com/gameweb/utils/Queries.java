package com.gameweb.utils;

/**
 * Created by Kamil on 2017-11-29.
 */

public class Queries
{

    static public final String S_USERS = "SELECT * FROM users";
    static public final String S_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";
    static public final String S_GAMES = "SELECT * FROM games";
    static public final String S_REVIEWS = "SELECT * FROM reviews";
    static public final String S_CATEGORIES = "SELECT * FROM categories";


    static public final String S_GET_DEF_AVATAR = "SELECT photo FROM photo_def WHERE id = 1";

    static public final String S_AVATAR = "SELECT avatar FROM users where id = (SELECT MAX(ID) FROM USERS)";

    static public final String I_USER = "INSERT INTO users (username, email, password) VALUES (?,?,?)";
    static public final String I_USER_W_AVATAR = "INSERT INTO users (username, email, password,avatar) VALUES (?,?,?,?)";
    static public final String I_GAME = "INSERT INTO games (title,about, masterid) VALUES (?,?,?)";
    static public final String I_REVIEW = "INSERT INTO reviews (parentid, content, rating) VALUES (?,?,?)";
    static public final String I_USER_ROLE = "INSERT INTO user_roles (username, role) VALUES (?,?)";
    static public final String I_CATEGORY_GAME = "INSERT INTO categories_games (cat_id, game_id) values (?,?)";


    static public final String U_USER_ABOUT = "UPDATE users set about = ? where username = ?";
    static public final String U_USER_AVATAR = "UPDATE users set avatar = ? where username = ?";

    static public final String U_GAME_ABOUT = "UPDATE games set about = ? where id = ?";
    static public final String U_GAME_COVER = "UPDATE games set cover = ? where id = ?";

}
