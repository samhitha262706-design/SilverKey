package com.silverkey.user;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.UUID;

@RegisterBeanMapper(User.class)
public interface UserRepository {

    @SqlUpdate("""
        INSERT INTO users (
            id,
            tenant_id,
            username,
            email,
            password_hash,
            created_at
        )
        VALUES (
            :id,
            :tenantId,
            :username,
            :email,
            :passwordHash,
            :createdAt
        )
        """)
    void save(@BindBean User user);

    @SqlQuery("""
        SELECT
            id,
            tenant_id AS tenantId,
            username,
            email,
            password_hash AS passwordHash,
            created_at AS createdAt
        FROM users
        WHERE id = :id
        """)
    Optional<User> findById(@Bind("id") UUID id);

    @SqlQuery("""
        SELECT
            id,
            tenant_id AS tenantId,
            username,
            email,
            password_hash AS passwordHash,
            created_at AS createdAt
        FROM users
        WHERE email = :email
        """)
    Optional<User> findByEmail(@Bind("email") String email);
}