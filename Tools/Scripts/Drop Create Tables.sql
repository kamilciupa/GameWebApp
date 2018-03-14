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


DROP TABLE public.games;

CREATE TABLE public.games
(
  id bigint NOT NULL DEFAULT nextval('games_id_seq'::regclass),
  title character varying(255),
  about character varying(1000000),
  cover bytea,
  release_date date,
  developer character varying(255),
  masterid bigint NOT NULL DEFAULT nextval('games_masterid_seq'::regclass),
  rating real,
  votes_amount integer,
  CONSTRAINT games_pkey PRIMARY KEY (id),
  CONSTRAINT games_masterid_fkey FOREIGN KEY (masterid)
      REFERENCES public.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT games_title_key UNIQUE (title)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.games
  OWNER TO postgres;


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
title varchar(500),
content varchar(1000000),
key_value bigserial references games(id),
author bigserial references users(id)
);




