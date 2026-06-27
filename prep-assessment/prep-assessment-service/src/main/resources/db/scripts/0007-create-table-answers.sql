create table if not exists answers
(
    id          bigserial primary key,
    question_id bigint      not null,
    text        text        not null,
    is_correct  boolean     not null,
    explanation text        not null,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz,

    constraint answers_question_id_questions_id foreign key (question_id) references questions (id),
    constraint uq_answers_question_id_text unique (question_id, text)
);