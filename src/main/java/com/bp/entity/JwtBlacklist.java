package com.bp.entity;

/**
 * JWT token blacklist, storing invalid tokens, which are not expired yet.
 */
public class JwtBlacklist {
	
	/**
	 * ID of token.
	 */
	private String id;
	
	/**
	 * Content of token.
	 */
	private String token;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
