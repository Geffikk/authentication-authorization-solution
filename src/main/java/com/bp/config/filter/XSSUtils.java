package com.bp.config.filter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;

public class XSSUtils {
	
	/**
	 * Encode input parameter values.
	 * @param value of parameter
	 * @return encoded value
	 */
	public static String stripXSS(String value) {
		if (value == null) {
			return null;
		}
		value = ESAPI.encoder()
			.canonicalize(value)
			.replaceAll("\0", "");
		return Jsoup.clean(value, Whitelist.none());
	}
}
