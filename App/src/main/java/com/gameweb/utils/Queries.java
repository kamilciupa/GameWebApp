package com.gameweb.utils;

/**
 * Created by Kamil on 2017-11-29.
 */

public class Queries
{

    static public final String S_USERS = "SELECT * FROM users";
    static public final String S_GAMES = "SELECT * FROM games";
    static public final String S_REVIEWS = "SELECT * FROM reviews";
    static public final String S_CATEGORIES = "SELECT * FROM categories";

    static public final String I_USER = "INSERT INTO users (username, email, password) VALUES (?,?,?)";
    static public final String I_GAME = "INSERT INTO games (title, masterid) VALUES (?,?)";
    static public final String I_REVIEW = "INSERT INTO reviews (parentid, content, rating) VALUES (?,?,?)";

    static public final String I_CATEGORY_GAME = "INSERT INTO categories_games (cat_id, game_id) values (?,?)";


    static public final String U_USER_ABOUT = "UPDATE users set about = ? where id = ?";
    static public final String U_USER_AVATAR = "UPDATE users set avatar = ? where id = ?";

    static public final String U_GAME_ABOUT = "UPDATE games set about = ? where id = ?";
    static public final String U_GAME_COVER = "UPDATE games set cover = ? where id = ?";

}
