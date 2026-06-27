create table if not exists user_test_sessions
(
    id           bigserial primary key,
    user_id      bigint      not null,
    test_id      bigint      not null,
    start_at     timestamptz not null,
    end_at       timestamptz,
    total_second int,
    is_completed boolean     not null,
    is_active    boolean     not null,
    created_at   timestamptz not null default now(),
    updated_at   timestamptz,

    constraint user_test_sessions_test_id_tests_id foreign key (test_id) references tests (id)
);