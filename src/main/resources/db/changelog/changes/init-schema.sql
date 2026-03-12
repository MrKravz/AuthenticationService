--liquibase formatted sql

--changeset init:1
CREATE TABLE accounts
(
    id            BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    login         VARCHAR(255),
    password_hash VARCHAR(255),
    user_id       BIGINT,
    role        VARCHAR(50),
    active        VARCHAR(50),
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP
);