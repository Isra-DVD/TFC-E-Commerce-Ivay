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

@Component
public class JwtTokenProvider {
	
	@Value("${security.jwt.key.private}")
	private String privateKey;
	
	private static final long JWT_EXPIRATION_DATE = 1800000000;  // milisegundos ; 1h = 3600s
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION_DATE);
		
		return Jwts.builder()
				   .subject(username)
				   .issuedAt(new Date())
				   .expiration(expireDate)
				   .signWith(getSignInKey(), Jwts.SIG.HS256)
				   .compact();
	}
	
	private SecretKey getSignInKey() {
		
		byte[] keyBytes = Decoders.BASE64.decode(privateKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String getSubjectFromToken(String token) {
		return extraClaim(token, Claims::getSubject);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extraClaim(token, Claims::getExpiration);
	}
	
	public boolean validateToken(String token) {
		Jwts.parser()
			.verifyWith(getSignInKey())
			.build()
			.parseSignedClaims(token);
		return true;
	}
	
	private <T> T extraClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				   .verifyWith(getSignInKey())
				   .build()
				   .parseSignedClaims(token)
				   .getPayload();
	}
}