drop table categories_games;
drop table categories;
drop table reviews;
drop table games;
drop table users;

create table users (
id BIGSERIAL PRIMARY KEY,
username varchar(255) unique,
email varchar(255) unique,
password varchar(255),
avatar bytea,
about varchar(1000000)
);

create table games (
id BIGSERIAL PRIMARY KEY,
title varchar(255) unique,
about varchar(1000000),
cover bytea,
masterId BIGSERIAL references users(id)
);

create table reviews (
id BIGSERIAL PRIMARY KEY,
parentId BIGSERIAL references games(id),
content varchar(1000000),
rating real 
);

create table categories (
id BIGSERIAL PRIMARY KEY,
name varchar(255)
);

create table categories_games (
cat_id BIGSERIAL references categories(id),
game_id BIGSERIAL references games(id)
);