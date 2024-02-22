-- liquibase formatted sql
-- changeset polar-bear:202402201205

CREATE TABLE IF NOT EXISTS users
(
    id                  bigserial,
    external_id         bigint,
    username            varchar(320)    UNIQUE NOT NULL,
    password            varchar(100)    NOT NULL,
    primary key (id)
);