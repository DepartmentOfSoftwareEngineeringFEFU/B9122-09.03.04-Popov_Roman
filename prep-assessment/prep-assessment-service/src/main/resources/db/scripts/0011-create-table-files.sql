create table if not exists files
(
    id            bigserial primary key,
    bucket        text        not null,
    object_key    text        not null,
    original_name text        not null,
    content_type  text        not null,
    size_bytes    bigint      not null check ( size_bytes >= 0 ),
    status        text        not null check ( status in ('PENDING_UPLOAD', 'UPLOADED') ),
    uploaded_by   bigint,
    created_at    timestamptz not null default now(),
    uploaded_at   timestamptz,
    updated_at    timestamptz,

    constraint uq_files_object_key unique (object_key)

);