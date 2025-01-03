-- loai bds
CREATE TABLE posts.residential_property
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);

-- tai lieu phap ly
CREATE TABLE posts.property_legal_document
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Noi that
CREATE TABLE posts.interior
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);

-- huong nha
CREATE TABLE posts.house_direction
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);

-- huong ban cong
CREATE TABLE posts.balcony_direction
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);




