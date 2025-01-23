--liquibase formatted sql

--changeset timofeev_vadim:2025-01-23--wallets

INSERT INTO wallets (balance)
VALUES (100.50),
       (0.01),
       (0.00),
       (123456789012345678901234567890.12),
       (99999999999999999999999999999.99),
       (1.11),
       (555.78),
       (1000000.00),
       (15.23),
       (0.50);