-- liquibase formatted sql

-- changeset shulgaea:1
CREATE TABLE notice
(
    id serial PRIMARY KEY,
    text_notice TEXT,
    chat_id integer,
    date_time_send_notice timestamp

);

-- changeset shulgaea:2
CREATE INDEX IF NOT EXISTS date_index ON notice (date_time_send_notice);

