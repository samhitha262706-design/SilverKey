CREATE TABLE organizations (
                               id UUID PRIMARY KEY,
                               name VARCHAR(255) NOT NULL UNIQUE,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);