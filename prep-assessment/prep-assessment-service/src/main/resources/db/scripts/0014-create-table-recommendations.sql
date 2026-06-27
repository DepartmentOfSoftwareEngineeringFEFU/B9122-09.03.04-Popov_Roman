create table if not exists recommendations
(
    id                  bigserial primary key,
    user_id             bigint      not null,
    recommendation_type text        not null check (recommendation_type in ('MATERIAL', 'TEST')),
    target_id           bigint,
    source_service      text        not null check ( source_service in ('MATERIAL_SERVICE', 'TEST_SERVICE')),
    topic_id            bigint      not null,
    subtopic            text        not null,
    reason              text        not null,
    priority            int,
--     status              text        not null check ( status in ('NEW', '') ),
    created_at          timestamptz not null default now(),
    updated_at          timestamptz,

    constraint fk_recommendations_topics foreign key (topic_id) references topics (id)

);