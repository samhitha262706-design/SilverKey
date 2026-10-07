CREATE TABLE permissions (
                             id UUID PRIMARY KEY,
                             name VARCHAR(100) NOT NULL UNIQUE,
                             description VARCHAR(255)
);