package com.silverkey.user;

import com.silverkey.exception.BusinessException;
import com.silverkey.security.JwtService;
import com.silverkey.tenant.Tenant;
import com.silverkey.tenant.TenantRepository;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TenantRepository tenantRepository;

    public UserService(
            UserRepository userRepository,
            JwtService jwtService,
            TenantRepository tenantRepository
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tenantRepository = tenantRepository;
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

    public String login(LoginRequest request) {

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

        return jwtService.generateToken(user.getId());
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
}