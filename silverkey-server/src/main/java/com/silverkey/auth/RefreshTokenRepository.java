package com.silverkey.auth;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.UUID;

@RegisterBeanMapper(RefreshToken.class)
public interface RefreshTokenRepository {

    @SqlUpdate("""
        INSERT INTO refresh_tokens (
            id,
            user_id,
            token,
            expires_at,
            created_at
        )
        VALUES (
            :id,
            :userId,
            :token,
            :expiresAt,
            :createdAt
        )
    """)
    void save(@BindBean RefreshToken refreshToken);

    @SqlQuery("""
        SELECT
            id,
            user_id AS userId,
            token,
            expires_at AS expiresAt,
            created_at AS createdAt
        FROM refresh_tokens
        WHERE token = :token
    """)
    Optional<RefreshToken> findByToken(@Bind("token") String token);

    @SqlUpdate("""
        DELETE FROM refresh_tokens
        WHERE token = :token
    """)
    void deleteByToken(@Bind("token") String token);
}