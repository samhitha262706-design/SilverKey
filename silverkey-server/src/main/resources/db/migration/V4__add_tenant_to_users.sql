ALTER TABLE users
    ADD COLUMN tenant_id UUID;

ALTER TABLE users
    ADD CONSTRAINT fk_user_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenants(id);