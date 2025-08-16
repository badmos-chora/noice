package com.noice.userbff.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Configuration
public class JWTConfig {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.algorithm}")
    private String algorithm;

    public SecretKey getSecretKey() {
        var key =new OctetSequenceKey.Builder(secretKey.getBytes())
                .algorithm(new JWSAlgorithm(algorithm))
                .build();
        return key.toSecretKey();
    }

    public JWSAlgorithm getAlgorithm() {
        return new JWSAlgorithm(algorithm);
    }

    public String generateToken(Authentication authentication) {
        var header = new JWSHeader.Builder(getAlgorithm())
                .type(JOSEObjectType.JWT)
                .build();
        Instant now = Instant.now();
        var roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        var builder = new JWTClaimsSet.Builder()
                .issuer("Connect")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(1, java.time.temporal.ChronoUnit.HOURS)));
        builder.claim("roles", roles);
        var user = (CustomUserDetail) authentication.getPrincipal();
        builder.claim("id", user.getUserID());
        var claims = builder.build();

        var key = getSecretKey();

        var jwt = new SignedJWT(header, claims);

        try {
            var signer = new MACSigner(key);
            jwt.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Error generating JWT",e);
        }
        return jwt.serialize();
    }
}
