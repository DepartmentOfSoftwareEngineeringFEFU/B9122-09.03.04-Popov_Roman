create table if not exists questions
(
    id               bigserial primary key,
    test_id          bigint       not null,
    topic_id         bigint       not null,
    subtopic         varchar(100) not null,
    grade            varchar(10)  not null check (grade in ('JUNIOR', 'MIDDLE', 'SENIOR')),
    wording_question text         not null,
    serial_number    int          not null,
    created_at       timestamptz  not null default now(),
    updated_at       timestamptz,

    constraint fk_questions_tests_id foreign key (test_id) references tests (id),
    constraint fk_questions_topics foreign key (topic_id) references topics (id)

);