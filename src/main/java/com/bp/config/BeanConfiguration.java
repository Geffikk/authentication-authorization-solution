package com.bp.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration.
 */
@Configuration
public class BeanConfiguration implements WebMvcConfigurer {
	
	/**
	 * H2 console configuration.
	 * @return servlet registration bean
	 */
	@Bean
	ServletRegistrationBean<WebServlet> h2servletRegistration() {
	    ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<>(new WebServlet());
	    registrationBean.addUrlMappings("/console/*");
	    return registrationBean;
	}
}
