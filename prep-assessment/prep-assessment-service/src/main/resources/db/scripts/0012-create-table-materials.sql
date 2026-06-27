create table if not exists materials
(
    id          bigserial primary key,
    title       text        not null,
    description text        not null,
    file_id     bigint      not null,
    topic_id    bigint,
    subtopic    text,
    grade       text check ( grade in ('JUNIOR', 'MIDDLE', 'SENIOR') ),
    created_at  timestamptz not null default now(),
    updated_at  timestamptz,

    constraint uq_materials_title unique (title),
    constraint fk_materials_file foreign key (file_id) references files (id),
    constraint fk_materials_topic foreign key (topic_id) references topics (id)
);