package com.bp.config.filter;

import com.bp.beans.Message;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Class filter malicious requests containing scripts.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class XSSFilter implements Filter {
	
	/**
	 * Control message after csrf attack.
	 */
	@Resource(name = "message")
	Message message;
	
	@Override
	public void init(FilterConfig filterConfig) {}
	
	@Override
	public void destroy() {}
	
	/**
	 * Filter malicious requests.
	 * @param request http request
	 * @param response http response
	 * @param chain filter chain
	 * @throws IOException exception
	 * @throws ServletException exception
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		XSSRequestWrapper wrappedRequest = new XSSRequestWrapper((HttpServletRequest) request);
		if (((HttpServletRequest) request).getRequestURI().equals("/login")) {
			chain.doFilter(request, response);
		} else {
			String body = IOUtils.toString(wrappedRequest.getReader());
			if (body.contains("=")) {
				message.setMsg(body.substring(body.indexOf("=") + 1));
			}
			if (!StringUtils.isBlank(body)) {
				body = XSSUtils.stripXSS(body);
				wrappedRequest.resetInputStream(body.getBytes());
			}
			chain.doFilter(wrappedRequest, response);
		}
	}
}
