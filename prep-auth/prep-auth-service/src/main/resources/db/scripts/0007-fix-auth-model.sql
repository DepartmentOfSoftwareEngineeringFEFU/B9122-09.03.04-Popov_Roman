alter table users
    add constraint uc_users_email unique (email);

alter table user_role
    drop constraint if exists uc_user_role_role_id;

alter table credentials
    add constraint uc_credentials_user_id unique (user_id);

insert into roles (name, code)
values ('Администратор', 'ROLE_ADMIN')
on conflict (code) do nothing;

insert into roles (name, code)
values ('Пользователь', 'ROLE_USER')
on conflict (code) do nothing;
