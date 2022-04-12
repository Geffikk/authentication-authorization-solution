package com.bp.config.oauth;

import com.bp.beans.OAuthJwtToken;
import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.service.IUserRoleService;
import com.bp.service.IUserService;
import com.bp.utility.CS;
import com.bp.utility.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OAuth2.0 success handler representation.
 */
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	/**
	 * Service of user.
	 */
	@Inject
	private IUserService userService;
	
	/**
	 * Service of user_role.
	 */
	@Inject
	private IUserRoleService userRoleService;
	
	/**
	 * JWT token utility functions.
	 */
	@Inject
	private JwtTokenUtil jwtTokenUtil;
	
	/**
	 * Authorization capability model.
	 */
	private AuthorizeModel authorizeModel = new AuthorizeModel();
	
	/**
	 * Bean for holding JWT auth token.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	private static final String GOOGLE = "GOOGLE";
	
	private static final String FACEBOOK = "FACEBOOK";
	
	/**
	 * OAuth2.0 processing success authentication.
	 * @param request http request
	 * @param response http response
	 * @param authentication authentication information
	 * @throws IOException exception
	 * @throws ServletException exception
	 */
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		UserEntity userEntity;
		
		if (authentication.getPrincipal() instanceof CustomOAuth2User) {
			CustomOAuth2User authUser = (CustomOAuth2User) authentication.getPrincipal();
			userEntity = userService.findUserByEmail(authUser.getEmail());
			userEntity = initializeUser(authUser.getEmail(), FACEBOOK, userEntity);
		} else {
			OidcUser authUser = (OidcUser) authentication.getPrincipal();
			userEntity = userService.findUserByEmail(authUser.getEmail());
			userEntity = initializeUser(authUser.getEmail(), GOOGLE, userEntity);
		}
		final String token = jwtTokenUtil.generateToken(userEntity.getEmail());
		userEntity.setJwtToken(token);
		userService.saveUser(userEntity, authorizeModel);
		oAuthJwtToken.setJwtToken("Bearer " + token);
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	/**
	 * Initialize new user instantiation.
	 * @param email of user
	 * @param provider of user
	 * @param userEntity instantiation
	 * @return new user
	 */
	private UserEntity initializeUser(String email, String provider, UserEntity userEntity) {
		if (userEntity == null) {
			userEntity = createUser(email, provider);
			userService.createUser(userEntity, authorizeModel);
			userRoleService.addRole(userEntity.getUserId(), CS.USER, authorizeModel);
		} else {
			userService.saveUser(updateUser(email, provider, userEntity), authorizeModel);
		}
		return userEntity;
	}
	
	/**
	 * Update user information.
	 * @param email of user
	 * @param provider of user
	 * @param userEntity instantiation
	 * @return updated user
	 */
	private UserEntity updateUser(String email, String provider, UserEntity userEntity) {
		userEntity.setEmail(email);
		userEntity.setAuthProvider(provider);
		userEntity.setActive(1);
		return userEntity;
	}
	
	/**
	 * Create user information.
	 * @param email of user
	 * @param provider of user
	 * @return new user entity
	 */
	private UserEntity createUser(String email, String provider) {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(email);
		userEntity.setAuthProvider(provider);
		userEntity.setActive(1);
		return userEntity;
	}
}
