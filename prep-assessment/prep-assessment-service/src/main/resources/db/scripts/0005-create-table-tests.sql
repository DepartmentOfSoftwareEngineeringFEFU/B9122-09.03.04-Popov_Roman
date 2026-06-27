create table if not exists tests
(
    id                bigserial primary key,
    title             varchar(100) not null,
    description       text,
    type              varchar(30)  not null check ( type in ('REGULAR', 'DIAGNOSTIC') ),
    grade             varchar(30) check ( grade in ('JUNIOR', 'MIDDLE', 'SENIOR') ),
    topic_id          bigint,
    learning_track_id bigint,

    created_at        timestamptz  not null default now(),
    updated_at        timestamptz,

    constraint uq_tests_title unique (title),
    constraint fk_tests_topic foreign key (topic_id) references topics (id),
    constraint fk_tests_learning_tracks foreign key (learning_track_id) references learning_tracks (id)
);