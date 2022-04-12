package com.bp.config.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

/**
 * OAuth2.0 User representation
 */
public class CustomOAuth2User implements OAuth2User {
	
	/**
	 * OAuth2.0 user.
	 */
	@Inject
	private OAuth2User user;
	
	public OAuth2User getUser() {
		return user;
	}
	
	public void setUser(OAuth2User user) {
		this.user = user;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return user.getAttributes();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}
	
	@Override
	public String getName() {
		return user.getAttribute("name");
	}
	
	public String getEmail() {
		return user.getAttribute("email");
	}
}
