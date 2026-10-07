package com.silverkey.audit;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditLog {

    private UUID id;
    private UUID userId;
    private String action;
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    public AuditLog(
            UUID id,
            UUID userId,
            String action,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}