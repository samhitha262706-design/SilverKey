CREATE TABLE tenants (
                         id UUID PRIMARY KEY,
                         organization_id UUID NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_tenant_organization
                             FOREIGN KEY (organization_id)
                                 REFERENCES organizations(id),

                         CONSTRAINT uq_tenant_organization_name
                             UNIQUE (organization_id, name)
);