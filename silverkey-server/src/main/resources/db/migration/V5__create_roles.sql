CREATE TABLE roles (
                       id UUID PRIMARY KEY,
                       tenant_id UUID NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_role_tenant
                           FOREIGN KEY (tenant_id)
                               REFERENCES tenants(id),

                       CONSTRAINT uq_role_tenant_name
                           UNIQUE (tenant_id, name)
);