package com.bp.utility;

import org.springframework.security.access.AccessDeniedException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * User utility class.
 */
public class UserUtility {
	
	/**
	 * Static function which check if password is valid.
	 * @param str password
	 * @return is valid true/false
	 */
	public static boolean isPasswordValid(String str) {
		char ch;
		// Must contain at least one capital.
		boolean capitalFlag = false;
		// Must contain at least one lower.
		boolean lowerCaseFlag = false;
		// Must contain at least one number.
		boolean numberFlag = false;
		
		if (str == null) {
			return false;
		}
		
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (Character.isDigit(ch)) {
				numberFlag = true;
			} else if (Character.isUpperCase(ch)) {
				capitalFlag = true;
			} else if (Character.isLowerCase(ch)) {
				lowerCaseFlag = true;
			}
			if (numberFlag && capitalFlag && lowerCaseFlag && i >= 8) {
				return true;
			}
		}
		
		if (!capitalFlag) {
			throw new AccessDeniedException("In password missing a capital letter !");
		} else if (!lowerCaseFlag) {
			throw new AccessDeniedException("In password missing a lower case letter !");
		} else if (!numberFlag) {
			throw new AccessDeniedException("In password missing a number !");
		}
		
		return false;
	}
	
	/**
	 * Check email validation.
	 * @param email of user
	 * @return true, if email is valid
	 * @throws AddressException exception
	 */
	public static boolean isEmailValid(String email) throws AddressException {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			throw new AddressException("Email address is not valid !");
		}
		return true;
	}
	
	/**
	 * Transform identification number to specific format.
	 * @param identification identification of user
	 * @return identification
	 * @throws Exception exception
	 */
	public static long transformIdentification(String identification) throws Exception {
		List<String> numbers = Arrays.asList(identification.split("/"));
		
		if (numbers.size() == 1) {
			return Long.parseLong(identification);
		}
		
		if (numbers.get(0).length() != 6 || numbers.get(1).length() != 4) {
			throw new Exception("Identification number is not valid !");
		}
		numbers = Arrays.asList(numbers.get(0).split("(?<=\\G..)"));
		
		// Month
		if (Integer.parseInt(numbers.get(1)) < 1 && Integer.parseInt(numbers.get(1)) > 13) {
			throw new Exception("Identification number is not valid !");
		}
		// Day
		if (Integer.parseInt(numbers.get(2)) < 1 && Integer.parseInt(numbers.get(2)) > 31) {
			throw new Exception("Identification number is not valid !");
		}
		return Long.parseLong(identification.replace("/", ""));
	}
	
	/**
	 * Convert double time format to string.
	 * @param time time
	 * @return time in str format
	 */
	public static String getTimeFormat(double time) {
		return time / 1000000 + " ms";
	}
}
