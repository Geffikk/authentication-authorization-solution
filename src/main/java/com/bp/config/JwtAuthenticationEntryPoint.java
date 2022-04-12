package com.bp.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * JWT authentication entry point.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	
	private static final long serialVersionUID = -7858869558953243875L;
	
	/**
	 * Send 404 response.
	 * @param request http request
	 * @param response http response
	 * @param authException exception
	 * @throws IOException exception
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
	                     AuthenticationException authException) throws IOException {
		
		final String expired = (String) request.getAttribute("expired");
		
		response.sendError(
			HttpServletResponse.SC_UNAUTHORIZED,
			Objects.requireNonNullElse(expired, "You are not authenticated !"));
	}
}
