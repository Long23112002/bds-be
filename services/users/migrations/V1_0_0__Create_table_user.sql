CREATE TABLE users.user
(
    id           BIGSERIAL PRIMARY KEY,
    code         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    password     TEXT,
    full_name    VARCHAR(50),
    phone_number VARCHAR(12),
    avatar       TEXT,
    wallet       DECIMAL(20, 2)        DEFAULT 0,
    is_admin     BOOLEAN      NOT NULL DEFAULT FALSE,
    is_block     BOOLEAN      NOT NULL DEFAULT FALSE,
    create_at    TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE users.role
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    code       VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN     DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users.user_roles
(
    user_id BIGINT,
    role_id BIGINT
);

CREATE TABLE users.permission
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    code       VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN     DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users.role_permission
(
    role_id       BIGINT,
    permission_id BIGINT
);

CREATE TABLE users.refresh_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(255) NOT NULL,
    user_id    BIGINT       NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    is_revoked BOOLEAN     DEFAULT FALSE
);

