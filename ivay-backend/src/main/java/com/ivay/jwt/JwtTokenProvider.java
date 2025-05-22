package com.ivay.jwt;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Responsible for creating and validating JSON Web Tokens (JWTs).
 *
 * Provides methods to:
 * - generate a signed JWT for an authenticated user
 * - extract claims such as subject (username) from a token
 * - validate token signature and expiration
 *
 * @since 1.0.0
 */
@Component
public class JwtTokenProvider {
    
    @Value("${security.jwt.key.private}")
    private String privateKey;
    
    private static final long JWT_EXPIRATION_DATE = 259200000; 		// 3 Days

    /**
     * Generates a JWT for the given authentication.
     *
     * The token contains:
     * - subject set to the username
     * - issued date set to now
     * - expiration date set to now + configured interval
     * - signature using the private key and HS256 algorithm
     *
     * @param authentication the authenticated principal
     * @return a signed JWT as String
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + JWT_EXPIRATION_DATE);
        
        return Jwts.builder()
                   .subject(username)
                   .issuedAt(now)
                   .expiration(expiry)
                   .signWith(getSignInKey(), Jwts.SIG.HS256)
                   .compact();
    }
    
    /**
     * Decodes the configured base64 private key into a SecretKey.
     *
     * @return HMAC-SHA key for signing and verifying tokens
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Extracts the subject (username) from the JWT.
     *
     * @param token the JWT string
     * @return the subject claim (username)
     */
    public String getSubjectFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Validates the token for signature correctness and expiration.
     *
     * @param token the JWT string
     * @return true if the token is well-formed, signed correctly, and not expired
     */
    public boolean validateToken(String token) {
        Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token);
        return true;
    }
    
    /**
     * Extracts a specific claim from the token using the given resolver.
     *
     * @param <T>           the type of the claim
     * @param token         the JWT string
     * @param claimResolver function that retrieves a claim from Claims
     * @return the extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    
    /**
     * Parses all claims from the token.
     *
     * @param token the JWT string
     * @return the Claims object containing all token data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .verifyWith(getSignInKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
}
