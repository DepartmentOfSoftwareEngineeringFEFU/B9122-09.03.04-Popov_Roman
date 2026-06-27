create table if not exists user_questions
(
    id          bigserial primary key,
    user_id     bigint      not null,
    question_id bigint      not null,
    answer_id   bigint      not null,
    is_correct  boolean     not null,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz,

    constraint fk_user_questions_question_id_questions_id foreign key (question_id) references questions (id),
    constraint fk_user_questions_answer_id_answers_id foreign key (answer_id) references answers (id)
);