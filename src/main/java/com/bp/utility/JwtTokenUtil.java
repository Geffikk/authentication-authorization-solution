package com.bp.utility;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Jwt token utility.
 */
@Component
public class JwtTokenUtil implements Serializable {
	
	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long JWT_TOKEN_VALIDITY = 2 * 60 * 60;
	
	/**
	 * Secret for hash jwt token.
	 */
	// TODO setup little harder secret
	@Value("${jwt.secret}")
	private String secret;
	
	/**
	 * Retrieve username from jwt token.
	 * @param token token body
	 * @return generated token
	 */
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	/**
	 * Retrieve expiration date from jwt token.
	 * @param token token body
	 * @return expiration date
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	/**
	 * Return claim for token.
	 * @param token token body
	 * @param claimsResolver claims resolver
	 */
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	/**
	 * For retrieving any information from token we will need the secret key.
	 * @param token token body
	 * @return claims
	 */
	private Claims getAllClaimsFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (IllegalArgumentException e) {
			throw new AccessDeniedException("You are not authenticated !");
		}
	}
	
	/**
	 * Check if the token has expired.
	 * @param token token body
	 * @return true, if token is expired
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	/**
	 * Generate token for user.
	 * @param username email of user
	 * @return generate token
	 */
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, username);
	}
	
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
			.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	/**
	 * Validate token.
	 * @param token token body
	 * @param userDetails user details
	 * @return true, if token is still valid
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}