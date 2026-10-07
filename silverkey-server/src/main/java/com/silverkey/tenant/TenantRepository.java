package com.silverkey.tenant;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.UUID;

@RegisterBeanMapper(Tenant.class)
public interface TenantRepository {

    @SqlUpdate("""
        INSERT INTO tenants (id, organization_id, name)
        VALUES (:id, :organizationId, :name)
    """)
    void create(
            @Bind("id") UUID id,
            @Bind("organizationId") UUID organizationId,
            @Bind("name") String name
    );

    @SqlQuery("""
        SELECT
            id,
            organization_id AS organizationId,
            name,
            created_at AS createdAt
        FROM tenants
        WHERE id = :id
    """)
    Optional<Tenant> findById(@Bind("id") UUID id);

    @SqlQuery("""
        SELECT
            id,
            organization_id AS organizationId,
            name,
            created_at AS createdAt
        FROM tenants
        WHERE organization_id = :organizationId
          AND name = :name
    """)
    Optional<Tenant> findByOrganizationAndName(
            @Bind("organizationId") UUID organizationId,
            @Bind("name") String name
    );
}