-- liquibase formatted sql
-- changeset polar-bear:202402201210

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id         bigint      NOT NULL,
    role_id         int         NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);