package com.noice.userbff.config;

import com.noice.userbff.enums.RoleType;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;

@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(prePostEnabled = false)
public class MethodSecurityConfig {

    private static final String SUPERADMIN_AUTHORITY = RoleType.ROLE_SUPER_ADMIN.name();


    @Bean
    public static MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    Advisor preAuthorizeAdvisor(MethodSecurityExpressionHandler expressionHandler, @Value("${security.superadmin-bypass.enabled}") boolean SUPERADMIN_BYPASS_ENABLED) {

        PreAuthorizeAuthorizationManager delegate = new PreAuthorizeAuthorizationManager();
        delegate.setExpressionHandler(expressionHandler);

        AuthorizationManager<MethodInvocation> mgr = (authSupplier, mi) -> {
            Authentication auth = authSupplier.get();
            boolean isSuper = auth != null && auth.getAuthorities().stream()
                    .anyMatch(a -> SUPERADMIN_AUTHORITY.equals(a.getAuthority()));

            if (isSuper && SUPERADMIN_BYPASS_ENABLED) {
                return new AuthorizationDecision(true);
            }
            return delegate.check(authSupplier, mi);
        };

        return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(mgr);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    Advisor postAuthorizeAdvisor(MethodSecurityExpressionHandler expressionHandler, @Value("${security.superadmin-bypass.enabled}") boolean SUPERADMIN_BYPASS_ENABLED) {
        PostAuthorizeAuthorizationManager delegate = new PostAuthorizeAuthorizationManager();
        delegate.setExpressionHandler(expressionHandler);

        AuthorizationManager<MethodInvocationResult> mgr = (authSupplier, result) -> {
            Authentication auth = authSupplier.get();
            boolean isSuper = auth != null && auth.getAuthorities().stream()
                    .anyMatch(a -> SUPERADMIN_AUTHORITY.equals(a.getAuthority()));
            return isSuper && SUPERADMIN_BYPASS_ENABLED ? new AuthorizationDecision(true) : delegate.check(authSupplier, result);
        };
        return AuthorizationManagerAfterMethodInterceptor.postAuthorize(mgr);
    }
}
