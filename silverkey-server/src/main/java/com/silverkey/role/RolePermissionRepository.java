package com.silverkey.role;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository {

    @SqlUpdate("""
        INSERT INTO role_permissions (
            role_id,
            permission_id
        )
        VALUES (
            :roleId,
            :permissionId
        )
        """)
    void assignPermission(
            @Bind("roleId") UUID roleId,
            @Bind("permissionId") UUID permissionId
    );

    @SqlQuery("""
        SELECT permission_id
        FROM role_permissions
        WHERE role_id = :roleId
        """)
    List<UUID> findPermissionIdsByRole(
            @Bind("roleId") UUID roleId
    );

    @SqlQuery("""
        SELECT EXISTS (
            SELECT 1
            FROM role_permissions
            WHERE role_id = :roleId
              AND permission_id = :permissionId
        )
        """)
    boolean exists(
            @Bind("roleId") UUID roleId,
            @Bind("permissionId") UUID permissionId
    );

    @SqlUpdate("""
        DELETE FROM role_permissions
        WHERE role_id = :roleId
          AND permission_id = :permissionId
        """)
    void removePermission(
            @Bind("roleId") UUID roleId,
            @Bind("permissionId") UUID permissionId
    );
}