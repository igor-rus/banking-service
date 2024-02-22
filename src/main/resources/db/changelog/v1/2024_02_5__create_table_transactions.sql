-- liquibase formatted sql
-- changeset polar-bear:202402201220

CREATE TABLE IF NOT EXISTS transactions
(
    id                  bigserial,
    external_id         uuid,
    operation_type      varchar(100)    NOT NULL,
    date_time           timestamp       NOT NULL,
    amount              decimal(10,2)   NOT NULL,
    PRIMARY KEY (id)
);
