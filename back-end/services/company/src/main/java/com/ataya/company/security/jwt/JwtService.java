package com.ataya.company.security.jwt;

import com.ataya.company.model.Worker;
import com.ataya.company.repo.WorkerRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    private final WorkerRepository workerRepository;
    @Value("${ataya.app.jwt.secret}")
    private String secret;

    @Value("${ataya.app.jwt.expiration.hours}")
    private long expirationHours;

    public JwtService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }


    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    private <T> T extractClaim(String jwt, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (SecurityException e) {
            log.error("Invalid JWT token signature: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw e;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        // inject roles

        HashMap<String, Object> extractClaims = new HashMap<>();
        String roles = userDetails.getAuthorities().toString()
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
                .replace("ROLE_", "");
        extractClaims.put("rls", roles);
        Worker worker = workerRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User not found with username: " + userDetails.getUsername()
                        )
                );
        extractClaims.put("cmp", worker.getCompanyId());
        extractClaims.put("str", worker.getStoreId());
        return generateToken(extractClaims, userDetails);
    }

    public String generateToken(HashMap<String, Object> extractClaims, UserDetails userDetails) {
        return buildToken(extractClaims, userDetails, expirationHours);
    }

    private String buildToken(HashMap<String, Object> extractClaims, UserDetails userDetails, long expirationHours) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expirationHours * 60 * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            final String username = extractUsername(jwt);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token is invalid: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String jwt) {
        final Date expiration = extractExpiration(jwt);
        return expiration.before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    // Additional utility methods
    public long getTokenExpirationTime(String token) {
        Date expiration = extractExpiration(token);
        return expiration.getTime();
    }

    public boolean isTokenExpiringSoon(String token, long thresholdMillis) {
        Date expiration = extractExpiration(token);
        return expiration.getTime() - System.currentTimeMillis() < thresholdMillis;
    }

    public Map<String, Object> getTokenClaims(String token) {
        return new HashMap<>(extractAllClaims(token));
    }

}
