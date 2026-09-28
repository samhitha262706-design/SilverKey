package com.silverkey.role;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.UUID;

@RegisterBeanMapper(Role.class)
public interface RoleRepository {

    @SqlUpdate("""
        INSERT INTO roles (
            id,
            tenant_id,
            name
        )
        VALUES (
            :id,
            :tenantId,
            :name
        )
        """)
    void create(
            @Bind("id") UUID id,
            @Bind("tenantId") UUID tenantId,
            @Bind("name") String name
    );

    @SqlQuery("""
        SELECT
            id,
            tenant_id AS tenantId,
            name,
            created_at AS createdAt
        FROM roles
        WHERE id = :id
        """)
    Optional<Role> findById(@Bind("id") UUID id);

    @SqlQuery("""
        SELECT
            id,
            tenant_id AS tenantId,
            name,
            created_at AS createdAt
        FROM roles
        WHERE tenant_id = :tenantId
          AND name = :name
        """)
    Optional<Role> findByTenantAndName(
            @Bind("tenantId") UUID tenantId,
            @Bind("name") String name
    );
}