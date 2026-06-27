create table if not exists learning_tracks
(
    id          bigserial primary key,
    code        varchar(50)  not null,
    title       varchar(100) not null,
    description text,
    created_at  timestamptz  not null default now(),
    updated_at  timestamptz,

    constraint uq_learning_tracks_code unique (code)
);