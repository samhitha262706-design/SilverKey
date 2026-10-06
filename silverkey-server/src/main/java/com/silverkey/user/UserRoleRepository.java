package com.silverkey.user;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository {

    @SqlUpdate("""
        INSERT INTO user_roles (
            user_id,
            role_id
        )
        VALUES (
            :userId,
            :roleId
        )
        """)
    void assignRole(
            @Bind("userId") UUID userId,
            @Bind("roleId") UUID roleId
    );

    @SqlQuery("""
        SELECT role_id
        FROM user_roles
        WHERE user_id = :userId
        """)
    List<UUID> findRoleIdsByUser(
            @Bind("userId") UUID userId
    );

    @SqlQuery("""
        SELECT EXISTS (
            SELECT 1
            FROM user_roles
            WHERE user_id = :userId
              AND role_id = :roleId
        )
        """)
    boolean exists(
            @Bind("userId") UUID userId,
            @Bind("roleId") UUID roleId
    );

    @SqlUpdate("""
        DELETE FROM user_roles
        WHERE user_id = :userId
          AND role_id = :roleId
        """)
    void removeRole(
            @Bind("userId") UUID userId,
            @Bind("roleId") UUID roleId
    );
}