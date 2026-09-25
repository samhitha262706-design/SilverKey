package com.silverkey.organization;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.UUID;

@RegisterBeanMapper(Organization.class)
public interface OrganizationRepository {

    @SqlUpdate("""
        INSERT INTO organizations (id, name)
        VALUES (:id, :name)
    """)
    void create(
            @Bind("id") UUID id,
            @Bind("name") String name
    );

    @SqlQuery("""
        SELECT
            id,
            name,
            created_at AS createdAt
        FROM organizations
        WHERE id = :id
    """)
    Optional<Organization> findById(
            @Bind("id") UUID id
    );

    @SqlQuery("""
        SELECT
            id,
            name,
            created_at AS createdAt
        FROM organizations
        WHERE name = :name
    """)
    Optional<Organization> findByName(
            @Bind("name") String name
    );
}