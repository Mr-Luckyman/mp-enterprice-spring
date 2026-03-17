--liquibase formatted sql
--changeset your-name:mp-191-1

CREATE TABLE IF NOT EXISTS books
(
    id             BIGSERIAL PRIMARY KEY,
    title          VARCHAR(200) NOT NULL,
    author         VARCHAR(100) NOT NULL,
    isbn           VARCHAR(255) NOT NULL,
    published_date DATE,
    available      BOOLEAN      NOT NULL DEFAULT TRUE
);