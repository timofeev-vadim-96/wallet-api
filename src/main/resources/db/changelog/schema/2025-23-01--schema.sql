--liquibase formatted sql

--changeset timofeev_vadim:2025-01-23--schema

create table wallets
(
    id      uuid not null primary key default gen_random_uuid(),
    balance numeric(38, 2)
--     version integer default 1
);