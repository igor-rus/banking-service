-- liquibase formatted sql
-- changeset polar-bear:202402201225

CREATE TABLE IF NOT EXISTS ledger_records
(
    id                  bigserial,
    transaction_id      bigint          NOT NULL,
    transaction_type    varchar(10)     NOT NULL,
    account_number      varchar(34)     NOT NULL,
    date_time           timestamp       NOT NULL,
    amount              bigint          NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);
