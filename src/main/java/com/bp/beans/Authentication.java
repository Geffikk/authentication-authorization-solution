package com.bp.beans;

import com.bp.entity.UserEntity;
import com.bp.service.IUserService;
import com.bp.service.UserService;
import com.bp.utility.JwtTokenUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Class represent authentication information about user.
 */
public class Authentication implements IAuthentication {
	
	/**
	 * Service of user.
	 */
	private final IUserService userService;
	
	/**
	 * Utility for token processing.
	 */
	private final JwtTokenUtil jwtTokenUtil;
	
	/**
	 * Bean for holding JWT auth token.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	public Authentication(UserService userService, JwtTokenUtil jwtTokenUtil) {
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	public UserEntity authorizeRequest(HttpServletRequest httpServletRequest) {
		try {
			String email;
			if (oAuthJwtToken.getJwtToken() != null) {
				email = jwtTokenUtil.getUsernameFromToken(oAuthJwtToken.getJwtToken().substring(7));
			} else {
				email = jwtTokenUtil.getUsernameFromToken(httpServletRequest.getHeader("Authorization").substring(7));
			}
			oAuthJwtToken.setJwtToken(null);
			return userService.findUserByEmail(email);
		} catch (Exception e) {
			return null;
		}
	}
}
