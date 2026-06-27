create table if not exists user_readiness_indicators
(
    id                       bigserial primary key,
    user_id                  bigint        not null unique,
    readiness_index          numeric(5, 2) not null,
    previous_readiness_index numeric(5, 2),
    readiness_delta          numeric(5, 2),
    readiness_status         varchar(50)   not null,
    progress_status          varchar(50)   not null,
    evaluated_topic_count    int           not null,
    mastered_topic_count     int           not null,
    weak_topic_count         int           not null,
    coverage                 numeric(5, 2) not null,
    calculated_at            timestamp     not null
);
