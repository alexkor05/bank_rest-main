--liquibase formatted sql

--changeset akorobov:1
CREATE TABLE processed_event
(
    event_id UUID PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL
);
