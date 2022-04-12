package com.bp.config;

import com.bp.beans.OAuthJwtToken;
import com.bp.repository.JwtBlacklistRepository;
import com.bp.service.MyUserDetailService;
import com.bp.utility.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT request filter implementation.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	/**
	 * User detail service.
	 */
	@Inject
	private MyUserDetailService userDetailsService;
	
	/**
	 * JWT token utility functions.
	 */
	private final JwtTokenUtil jwtTokenUtil;
	
	/**
	 * JWT blacklist.
	 */
	public JwtBlacklistRepository jwtBlacklistRepository;
	
	/**
	 * JWT auth token holder.
	 */
	@Resource(name = "OAuthJwtToken")
	OAuthJwtToken oAuthJwtToken;
	
	public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, JwtBlacklistRepository jwtBlacklistRepository) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.jwtBlacklistRepository = jwtBlacklistRepository;
	}
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	/**
	 * JWT filter method, which allow or restrict access.
	 * @param request http request
	 * @param response http response
	 * @param chain filter chain
	 * @throws ServletException exception
	 * @throws IOException exception
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		
		String requestTokenHeader = request.getHeader("Authorization");
		
		if (oAuthJwtToken.getJwtToken() != null) {
			requestTokenHeader = oAuthJwtToken.getJwtToken();
		}
		
		String username = null;
		String jwtToken = null;
		String blackList;
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
		
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			blackList = jwtBlacklistRepository.findByTokenEquals(jwtToken);
			
			if (blackList == null) {
				try {
					username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				} catch (SignatureException ex) {
					logger.info("Invalid JWT Signature");
				} catch (MalformedJwtException ex) {
					logger.info("Invalid JWT token");
				} catch (ExpiredJwtException ex) {
					logger.info("Expired JWT token");
					request.setAttribute("expired",ex.getMessage());
				} catch (UnsupportedJwtException ex) {
					logger.info("Unsupported JWT exception");
				} catch (IllegalArgumentException ex) {
					logger.info("Jwt claims string is empty");
				}
			} else {
				logger.info("You are not authenticated !");
				oAuthJwtToken.setJwtToken("null");
			}
		}
		
		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				
				usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}
}
