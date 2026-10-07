package com.silverkey.audit;

import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AuditLogRepository {

    @SqlUpdate("""
        INSERT INTO audit_logs (
            id,
            user_id,
            action,
            created_at
        )
        VALUES (
            :id,
            :userId,
            :action,
            :createdAt
        )
    """)
    void save(@BindBean AuditLog auditLog);
}