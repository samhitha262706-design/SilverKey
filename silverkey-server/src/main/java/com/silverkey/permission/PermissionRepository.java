package com.silverkey.permission;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.UUID;

@RegisterBeanMapper(Permission.class)
public interface PermissionRepository {

    @SqlUpdate("""
        INSERT INTO permissions (
            id,
            name,
            description
        )
        VALUES (
            :id,
            :name,
            :description
        )
        """)
    void create(
            @Bind("id") UUID id,
            @Bind("name") String name,
            @Bind("description") String description
    );

    @SqlQuery("""
        SELECT
            id,
            name,
            description
        FROM permissions
        WHERE id = :id
        """)
    Optional<Permission> findById(@Bind("id") UUID id);

    @SqlQuery("""
        SELECT
            id,
            name,
            description
        FROM permissions
        WHERE name = :name
        """)
    Optional<Permission> findByName(@Bind("name") String name);
}