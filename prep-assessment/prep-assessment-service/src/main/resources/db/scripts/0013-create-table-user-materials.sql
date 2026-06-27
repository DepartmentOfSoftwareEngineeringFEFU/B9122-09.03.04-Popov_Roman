create table if not exists user_materials
(
    id          bigserial primary key,
    user_id     bigint      not null,
    material_id bigint      not null,
    is_liked    boolean     not null,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz,

    constraint uq_user_id_material_id unique (user_id, material_id),
    constraint fk_user_materials_material_id_materials_id foreign key (material_id) references materials (id)
);