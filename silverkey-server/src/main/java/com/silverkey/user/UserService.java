package com.silverkey.user;

import com.silverkey.audit.AuditLogService;
import com.silverkey.auth.RefreshToken;
import com.silverkey.auth.RefreshTokenRepository;
import com.silverkey.exception.BusinessException;
import com.silverkey.security.JwtService;
import com.silverkey.tenant.Tenant;
import com.silverkey.tenant.TenantRepository;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TenantRepository tenantRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuditLogService auditLogService;

    public UserService(
            UserRepository userRepository,
            JwtService jwtService,
            TenantRepository tenantRepository,
            RefreshTokenRepository refreshTokenRepository,
            AuditLogService auditLogService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tenantRepository = tenantRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.auditLogService = auditLogService;
    }

    public void register(RegisterUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(
                    "Email already exists.",
                    Response.Status.CONFLICT
            );
        }

        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() ->
                        new BusinessException(
                                "Tenant not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setTenantId(tenant.getId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        String passwordHash = BCrypt.hashpw(
                request.getPassword(),
                BCrypt.gensalt()
        );

        user.setPasswordHash(passwordHash);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BusinessException(
                                "Invalid email or password.",
                                Response.Status.UNAUTHORIZED
                        )
                );

        boolean passwordMatches = BCrypt.checkpw(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new BusinessException(
                    "Invalid email or password.",
                    Response.Status.UNAUTHORIZED
            );
        }
        auditLogService.log(
                user.getId(),
                "USER_LOGIN"
        );

        // Generate short-lived access token
        String accessToken =
                jwtService.generateToken(user.getId());

        // Generate long-lived refresh token
        String refreshTokenValue = generateRefreshToken();

        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID(),
                user.getId(),
                refreshTokenValue,
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now()
        );

        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(
                accessToken,
                refreshTokenValue
        );
    }

    public User getUserById(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found.",
                                Response.Status.NOT_FOUND
                        )
                );
    }

    private String generateRefreshToken() {

        byte[] randomBytes = new byte[32];

        new SecureRandom().nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    public String refreshAccessToken(String refreshTokenValue) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(refreshTokenValue)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Invalid refresh token.",
                                        Response.Status.UNAUTHORIZED
                                )
                        );

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByToken(refreshTokenValue);

            throw new BusinessException(
                    "Refresh token has expired.",
                    Response.Status.UNAUTHORIZED
            );
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        return jwtService.generateToken(user.getId());
    }
}