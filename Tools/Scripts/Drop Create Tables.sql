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
release_date date,
developer varchar(255),
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


create table commments 
(
id bigserial primary key,
content varchar(1000000),
parent bigserial,
type varchar(1) check (type in ('R','G')),
key_value bigserial,
author bigserial references users(id)
);

drop table reviews;
create table reviews 
(
id bigserial primary key,
content varchar(1000000),
key_value bigserial references games(id),
author bigserial references users(id)
);




