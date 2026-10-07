package com.silverkey.audit;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(
            UUID userId,
            String action
    ) {
        AuditLog auditLog = new AuditLog(
                UUID.randomUUID(),
                userId,
                action,
                LocalDateTime.now()
        );

        auditLogRepository.save(auditLog);
    }
}