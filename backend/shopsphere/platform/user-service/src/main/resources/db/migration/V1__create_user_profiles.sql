CREATE TABLE user_profiles
(
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL UNIQUE,

    first_name VARCHAR(100) NOT NULL,

    last_name VARCHAR(100) NOT NULL,

    phone_number VARCHAR(20),

    gender VARCHAR(30),

    date_of_birth DATE,

    profile_image_url VARCHAR(500),

    bio VARCHAR(1000),

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_user_profile_user_id
ON user_profiles(user_id);