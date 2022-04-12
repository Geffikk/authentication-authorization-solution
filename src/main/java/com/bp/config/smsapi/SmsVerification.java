package com.bp.config.smsapi;

import com.bp.beans.OTP;
import com.bp.entity.UserEntity;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Random;

public class SmsVerification implements ISmsVerification {
	
	/**
	 * OTP bean, which hold OTP code for mobile otp authentication.
	 */
	@Resource(name = "OTP")
	OTP otpBean;
	
	public void smsVerify(UserEntity authenticatedUser) {
		String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
		otpBean.setOtp(otp);
		
		String phone = authenticatedUser.getPhone().replace(" ", "");
		
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://telesign-telesign-send-sms-verification-code-v1.p.rapidapi.com/sms-verification-code?phoneNumber=" + phone + "&verifyCode=" + otp))
			.header("x-rapidapi-key", "506e6dc1a7msh758159841c183f0p15a07ejsne1d6d7ab70ed")
			.header("x-rapidapi-host", "telesign-telesign-send-sms-verification-code-v1.p.rapidapi.com")
			.method("POST", HttpRequest.BodyPublishers.noBody())
			.build();
		HttpResponse<String> response;
		
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
