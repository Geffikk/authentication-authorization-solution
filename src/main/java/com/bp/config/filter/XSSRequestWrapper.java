package com.bp.config.filter;

import static com.bp.config.filter.XSSUtils.stripXSS;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Wrap XSS filter, contain request processing, filtering and returning response.
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {
	
	/**
	 * Raw data of request.
	 */
	private byte[] rawData;
	
	/**
	 * Http server request.
	 */
	private final HttpServletRequest request;
	
	/**
	 * Servlet stream.
	 */
	private final ResettableServletInputStream servletStream;
	
	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
		this.servletStream = new ResettableServletInputStream();
	}
	
	/**
	 * Replace old request stream.
	 * @param newRawData request raw stream data
	 */
	public void resetInputStream(byte[] newRawData) {
		rawData = newRawData;
		servletStream.stream = new ByteArrayInputStream(newRawData);
	}
	
	/**
	 * Return input stream of servlet.
	 * @return input stream of servlet
	 * @throws IOException exception
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (rawData == null) {
			rawData = IOUtils.toByteArray(this.request.getReader(), String.valueOf(Charsets.UTF_8));
			servletStream.stream = new ByteArrayInputStream(rawData);
		}
		return servletStream;
	}
	
	/**
	 * Return buffered reader of input servlet stream.
	 * @return buffered reader
	 * @throws IOException exception
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		if (rawData == null) {
			rawData = IOUtils.toByteArray(this.request.getReader(), String.valueOf(Charsets.UTF_8));
			servletStream.stream = new ByteArrayInputStream(rawData);
		}
		return new BufferedReader(new InputStreamReader(servletStream));
	}
	
	/**
	 * Servlet input stream representation.
	 */
	private static class ResettableServletInputStream extends ServletInputStream {
		
		/**
		 * Input stream.
		 */
		private InputStream stream;
		
		@Override
		public int read() throws IOException {
			return stream.read();
		}
		
		@Override
		public boolean isFinished() {
			return false;
		}
		
		@Override
		public boolean isReady() {
			return false;
		}
		
		@Override
		public void setReadListener(ReadListener readListener) { }
	}
	
	/**
	 * Return encoded values from request.
	 * @param parameter of request
	 * @return encoded values
	 */
	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = stripXSS(values[i]);
		}
		return encodedValues;
	}
	
	/**
	 * Return parameter of request.
	 * @param parameter of request
	 * @return encoded parameter
	 */
	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		return stripXSS(value);
	}
	
	/**
	 * Return header of request.
	 * @param name of header
	 * @return encoded header
	 */
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		return stripXSS(value);
	}
	
	/**
	 * Return headers of requests.
	 * @param name of headers
	 * @return encoded headers
	 */
	@Override
	public Enumeration<String> getHeaders(String name) {
		List<String> result = new ArrayList<>();
		Enumeration<String> headers = super.getHeaders(name);
		while (headers.hasMoreElements()) {
			String header = headers.nextElement();
			String[] tokens = header.split(",");
			for (String token : tokens) {
				result.add(stripXSS(token));
			}
		}
		return Collections.enumeration(result);
	}
}
