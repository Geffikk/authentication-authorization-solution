package com.bp.beans;

/**
 * Class for represent JWT auth token model.
 */
public class OAuthJwtToken {
	
	/**
	 * JWT token.
	 */
	private String jwtToken;
	
	public String getJwtToken() {
		return jwtToken;
	}
	
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
}
