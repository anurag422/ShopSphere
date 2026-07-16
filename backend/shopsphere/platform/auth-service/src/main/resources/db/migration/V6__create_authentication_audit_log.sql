CREATE TABLE authentication_audit_logs
(
    id BIGSERIAL PRIMARY KEY,

    user_id UUID NOT NULL,

    event_type VARCHAR(50) NOT NULL,

    ip_address VARCHAR(255) NOT NULL,

    user_agent VARCHAR(1000) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_auth_log_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
);