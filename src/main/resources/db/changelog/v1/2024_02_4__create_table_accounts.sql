-- liquibase formatted sql
-- changeset polar-bear:202402201215

CREATE TABLE IF NOT EXISTS accounts
(
    id                  bigserial,
    external_id         bigint          NOT NULL,
    owner_id            bigint          NOT NULL,
    account_number      varchar(34)     NOT NULL,
    account_type        varchar(100)    NOT NULL,
    balance             bigint          NOT NULL,
    currency            varchar(3)      NOT NULL,
    primary key (id),
    FOREIGN KEY (owner_id) REFERENCES users(id)
);