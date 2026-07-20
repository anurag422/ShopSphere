CREATE TABLE addresses
(
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    full_name VARCHAR(100) NOT NULL,

    phone_number VARCHAR(15) NOT NULL,

    address_line1 VARCHAR(255) NOT NULL,

    address_line2 VARCHAR(255),

    city VARCHAR(100) NOT NULL,

    state VARCHAR(100) NOT NULL,

    country VARCHAR(100) NOT NULL,

    postal_code VARCHAR(10) NOT NULL,

    address_type VARCHAR(20),

    default_shipping BOOLEAN DEFAULT FALSE,

    default_billing BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_address_user
ON addresses(user_id);