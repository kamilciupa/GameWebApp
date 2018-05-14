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
    static public final String S_GAMES_ORDERED_BY_RATING = "select * from games order by rating desc nulls last,votes_sum desc limit 10";
    static public final String S_GAME_BY_TITLE = "SELECT * FROM games WHERE title = ?";
    static public final String S_GAMETITLE_BY_ID = "SELECT title FROM games WHERE id =?";
    static public final String S_REVIEWS = "SELECT * FROM reviews";
    static public final String S_CATEGORIES = "SELECT * FROM categories";
    static public final String S_IS_VOTED = "SELECT vote FROM user_vote where game_id = ? and user_id = ?";
    static public final String S_GAMES_LIKE = "SELECT * FROM games WHERE upper(title) like ?";
    static public final String S_TAGS_FOR_GAME = "SELECT nam";

    static public final String S_GET_DEF_AVATAR = "SELECT photo FROM photo_def WHERE id = 1";

    static public final String S_AVATAR = "SELECT avatar FROM users where id = (SELECT MAX(ID) FROM USERS)";

    static public final String I_USER = "INSERT INTO users (username, email, password) VALUES (?,?,?)";
    static public final String I_USER_W_AVATAR = "INSERT INTO users (username, email, password,avatar) VALUES (?,?,?,?)";
//    static public final String I_GAME = "INSERT INTO games (title,about, developer, release_date,cover,  masterid) VALUES (?,?,?,?,?,?)";
    static public final String I_GAME = "INSERT INTO games (title,about, developer, cover,  masterid) VALUES (?,?,?,?,?)";
    static public final String I_REVIEW = "INSERT INTO reviews (title, content, key_value, author) VALUES (?,?,?,?)";
    static public final String I_USER_ROLE = "INSERT INTO user_roles (username, role) VALUES (?,?)";
    static public final String I_CATEGORY_GAME = "INSERT INTO categories_games (cat_id, game_id) values (?,?)";
    static public final String I_USER_VOTE_MAP = "INSERT INTO user_vote (user_id, game_id, vote)\n" +
            " values (?,(select id from games where title = ?),?);";


    static public final String U_USER_ABOUT = "UPDATE users set about = ? where username = ?";
    static public final String U_USER_AVATAR = "UPDATE users set avatar = ? where username = ?";

    static public final String U_GAME_ABOUT = "UPDATE games set about = ? where id = (select id from games where title = ?)";
    static public final String U_GAME_DEVELOPER = "UPDATE games set developer = ? where id = (select id from games where title = ?)";
    static public final String U_GAME_COVER = "UPDATE games set cover = ? where id = (select id from games where title = ?)";

    public static final String S_REVIEW_BY_ID = "SELECT * FROM reviews WHERE id = ?";
    public static final String S_GET_GAMES_TITLES = "SELECT title FROM games ORDER BY title";
    public static final String U_GAME_RATING = "UPDATE games SET votes_amount = ? , votes_sum = ?, rating = ? where title = ?";
    public static final String I_COMMENT = "INSERT INTO commments (content, parent, type, key_value, author) values (?,?,?,?,?)";
    public static final String S_GET_REVIEWS_PER_GAME = "SELECT id, title, content, key_value, author FROM reviews where key_value = (SELECT id FROM games where title = ?) ";
    public static final String S_VOTE_MAPPING = "SELECT count(1) vote FROM user_vote where user_id = ? and game_id = (select id from games where title = ?)";
    public static final String S_GET_COMMS = "SELECT c.*, u.username,u.avatar  FROM commments c, users u WHERE c.type = 'G' and c.key_value = (SELECT id FROM games WHERE title = ?) and c.author = u.id";
    public static final String S_GET_CHILD_COMMS = "SELECT * FROM comments WHERE parent = ?";
    public static final String U_USER_INFO = "UPDATE users set about = ?  , email = ? where username = ?";
    public static final String S_GAMES_USER = "select title from games where masterid = (select id from users where username = ?) ;";
    public static final String S_GAME_MASTER = "select a.masterid - b.id diff from (\n" +
            "select masterid from games where title = ?) a\n" +
            ", (select id from users where username = ?) b ; ";
    public static final String D_GAME_VOTES = "delete from user_vote where game_id = (select id from games where title = ?) ;";
    public static final String D_GAME_REV = "delete from reviews where key_value = (select id from games where title = ?);";
    public static final String D_GAME_COM = "delete from commments where key_value = (select id from games where title = ?);";
    public static final String D_GAME = "DELETE FROM games where title = ? and masterid = (select id from users where username = ?) ";
    public static final String SELECT_COUNT_1_FROM_GAMES = "select count(1) from games";
    public static final String S_AMOUN_REVS_PER_GAME = "select count(1) from reviews where key_value = (select id from games where title = ?)";
    public static final String S_CAT_BY_NAME = "SELECT * FROM categories WHERE UPPER(name) = upper(?)";
  public static final String  S_CAT_BY_GAME =  "select * from categories where id = (select cat_id from categories_games where game_id = ?)";
}
