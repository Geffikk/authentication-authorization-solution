package com.bp.beans;

import com.bp.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface IAuthentication {
	/**
	 * This method authorize user and return his object instantiation.
	 * @param httpServletRequest http request.
	 * @return authenticated user object
	 */
	UserEntity authorizeRequest(HttpServletRequest httpServletRequest);
}
