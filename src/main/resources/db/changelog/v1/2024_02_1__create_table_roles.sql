-- liquibase formatted sql
-- changeset polar-bear:202402201200

CREATE TABLE IF NOT EXISTS roles
(
    id              serial,
    external_id     int,
    name            varchar(50)    NOT NULL,
    PRIMARY KEY (id)
);