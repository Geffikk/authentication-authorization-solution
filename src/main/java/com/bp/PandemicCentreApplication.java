package com.bp;

import com.bp.beans.Message;
import com.bp.beans.OAuthJwtToken;
import com.bp.beans.OTP;
import com.bp.beans.RegistrationID;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

/**
 * Main class.
 */
@SpringBootApplication
public class PandemicCentreApplication {
	/**
	 * Start up application method.
	 * @param args arguments of application
	 */
	public static void main(final String[] args) {
		SpringApplication.run(PandemicCentreApplication.class, args);
	}
	
	@Bean
	BeanFactoryPostProcessor beanFactoryPostProcessor(ApplicationContext beanRegistry) {
		return beanFactory -> {
			genericApplicationContext(
				(BeanDefinitionRegistry)
				((AnnotationConfigServletWebServerApplicationContext) beanRegistry)
					.getBeanFactory());
		};
	}
	
	void genericApplicationContext(BeanDefinitionRegistry beanRegistry) {
		ClassPathBeanDefinitionScanner beanDefinitionScanner = new ClassPathBeanDefinitionScanner(beanRegistry);
		beanDefinitionScanner.addIncludeFilter(removeModelAndEntitiesFilter());
		beanDefinitionScanner.scan("com.bp");
	}
	
	static TypeFilter removeModelAndEntitiesFilter() {
		return (MetadataReader mr, MetadataReaderFactory mrf) -> !mr.getClassMetadata()
			.getClassName()
			.endsWith("Model");
	}
	
	@Bean
	@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public RegistrationID websocketRegistrationID() {
		return new RegistrationID();
	}
	
	@Bean
	@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public OAuthJwtToken requestOAuthJwtToken() {
		return new OAuthJwtToken();
	}
	
	@Bean
	@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Message requestMessage() {
		return new Message();
	}
	
	@Bean
	@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public OTP requestOtp() {
		return new OTP();
	}
}
