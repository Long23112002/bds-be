-- Tạo bảng package
CREATE TABLE posts.package
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    level       INT          NOT NULL,
    description TEXT,
    is_deleted  BOOLEAN   DEFAULT FALSE
);

-- Tạo bảng package_price
CREATE TABLE posts.package_price
(
    id         BIGSERIAL PRIMARY KEY,
    package_id BIGINT         NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    unit       VARCHAR(50)    NOT NULL,
    validity   BIGINT         NOT NULL,
    is_enable  BOOLEAN   DEFAULT TRUE,
    is_deleted BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng package_price_transaction
CREATE TABLE posts.package_price_transaction
(
    id               BIGSERIAL PRIMARY KEY,
    post_id          BIGINT NOT NULL,
    package_price_id BIGINT NOT NULL,
    start_date       DATE   NOT NULL,
    end_date         DATE   NOT NULL,
    start_time       TIME   NOT NULL,
    status           VARCHAR,
    is_deleted       BOOLEAN DEFAULT FALSE
);


create table posts.logs_transaction
(
    id                        BIGSERIAL PRIMARY KEY,
    package_price_transaction BIGINT         NOT NULL,
    userApproved              BIGINT         NOT NULL,
    price                     DECIMAL(10, 2) NOT NULL,
    status                    VARCHAR,
    old_status                VARCHAR,
    is_deleted                BOOLEAN   DEFAULT FALSE,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);