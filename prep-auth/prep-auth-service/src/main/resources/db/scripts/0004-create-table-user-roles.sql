CREATE TABLE user_role
(
    id      bigserial NOT NULL,
    role_id int8 NOT NULL,
    user_id int8 NOT NULL,
    CONSTRAINT pk_user_role_id PRIMARY KEY (id),
    CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT uc_user_role_user_id UNIQUE (user_id),
    CONSTRAINT uc_user_role_role_id UNIQUE (role_id)
);
