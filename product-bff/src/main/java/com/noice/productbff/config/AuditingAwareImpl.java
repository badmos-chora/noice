package com.noice.productbff.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditingAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(Jwt.class::isInstance)
                .map(Jwt.class::cast)
                .map(jwt -> jwt.getClaim("id"))
                .flatMap(this::parseToLong);
    }

    private Optional<Long> parseToLong(Object idClaim) {
        return switch (idClaim) {
            case Number n -> Optional.of(n.longValue());
            case String s -> {
                try {
                    yield Optional.of(Long.parseLong(s));
                } catch (NumberFormatException e) {
                    yield Optional.empty();
                }
            }
            case null, default -> Optional.empty();
        };
    }
}
