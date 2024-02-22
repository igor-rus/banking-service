-- liquibase formatted sql
-- changeset polar-bear:202402201230

INSERT INTO roles(external_id, name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');
INSERT INTO users(external_id, username, password) VALUES (1, 'admin@email.com', '$2a$10$smhrAduz5PHwI.47tVzjFedF.kWcIBRKWgw5WOloA/E9qVekgSGZW'),
                                                    (2, 'user@email.com', '$2a$10$IItUUlaaJjpLrb1k6IgBAuipyrKADlaH76UwqLfbR/gassmbrfj4a');
INSERT INTO users_roles(user_id, role_id) VALUES (1, 1), (2, 2);
INSERT INTO accounts(external_id, owner_id, account_number, account_type, balance, currency)
VALUES (1, 1, 'GB33BUKB20201555555555', 'REGULAR', 0, 'EUR'),
       (2, 1, 'GB33BUKB20201555555556', 'SAVINGS', 0, 'EUR'),
       (3, 2, 'GB33BUKB20201555555557', 'REGULAR', 0, 'EUR'),
       (4, 2, 'GB33BUKB20201555555558', 'REGULAR', 0, 'EUR');
