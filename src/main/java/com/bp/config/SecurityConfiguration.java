package com.bp.config;

import com.bp.config.oauth.OAuth2LoginSuccessHandler;
import com.bp.service.CustomOAuth2UserService;
import com.bp.service.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import javax.inject.Inject;

/**
 * Spring Security config.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	/**
	 * User detail service.
	 */
	// Annotation dependency injection due to circular dependencies
	@Inject
	private MyUserDetailService userDetailsService;
	
	/**
	 * Request filter service.
	 */
	// Annotation dependency injection due to circular dependencies
	@Inject
	private JwtRequestFilter jwtRequestFilter;
	
	/**
	 * OAuth service custom implementation.
	 */
	private final CustomOAuth2UserService customOAuth2UserService;
	
	/**
	 * JWT authentication entry point.
	 */
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	/**
	 * OAuth success handler.
	 */
	private final OAuth2LoginSuccessHandler oAuthLoginSuccessHandler;
	
	public SecurityConfiguration(
		CustomOAuth2UserService customOAuth2UserService,
		JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
		OAuth2LoginSuccessHandler oAuthLoginSuccessHandler) {
		this.customOAuth2UserService = customOAuth2UserService;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.oAuthLoginSuccessHandler = oAuthLoginSuccessHandler;
	}
	
	/**
     * Security Configuration.
     * @param http Http security
     * @throws Exception Exception handler
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
	        .csrf().disable()
	        .formLogin().permitAll().and()
	        .authorizeRequests()
	        .antMatchers("/api/v1/register", "/api/v1/verify", "/oauth/authenticate", "/api/v1/generate/**").permitAll()
	        .antMatchers("/login", "/mobile/login", "api/v1/email/login", "/api/v1/login").permitAll()
	        .and()
	        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and()
            .oauth2Login()
	        .loginPage("/login")
	        .userInfoEndpoint().userService(customOAuth2UserService).and().successHandler(oAuthLoginSuccessHandler)
	        .and()
	        .headers()
	        .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
	        .and()
	        .xssProtection()
	        .and()
	        .contentSecurityPolicy("script-src 'self'")
	        .and()
	        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
        
	    // Add a filter to validate the tokens with every request
	    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
	
	/**
	 * Authentication setup details due to connecting oauth user with own user entity representation.
	 * @param auth authentication
	 * @throws Exception exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
 
	@Bean("authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/**
	 * Password encoder (B-crypt).
	 * @return encrypted password.
	 */
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
