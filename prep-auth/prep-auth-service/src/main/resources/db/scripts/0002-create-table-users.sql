create table users
(
    id    bigserial primary key,
    login varchar(255) not null unique,
    email varchar(255) not null,
    grade text check ( grade in ('JUNIOR', 'MIDDLE', 'SENIOR') ),
    learning_track_id bigint
);