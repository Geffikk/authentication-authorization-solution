package com.bp.service;

import com.bp.beans.OTP;
import com.bp.model.email.MailProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendingMailService implements ISendingMailService {
	
	/**
	 * SMTP properties.
	 */
	private final MailProperties mailProperties;
	
	/**
	 * Template configuration (load html, which is send on email).
	 */
	private final Configuration templates;
	
	/**
	 * OTP bean, which hold otp code.
	 */
	@Resource(name = "OTP")
	OTP otpCode;
	
	SendingMailService(MailProperties mailProperties, Configuration templates) {
		this.mailProperties = mailProperties;
		this.templates = templates;
	}
	
	@Override
	public String sendVerificationMail(String toEmail, String verificationCode, int userId) {
		String subject = "Please verify your email";
		String body = "";
		try {
			Template t = templates.getTemplate("email-verification.ftl");
			Map<String, String> map = new HashMap<>();
			map.put("VERIFICATION_URL", mailProperties.getVerificationApi() + verificationCode + "/" + userId);
			body = FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
		} catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
		sendMail(toEmail, subject, body);
		return mailProperties.getVerificationApi() + verificationCode + "/" + userId;
	}
	
	@Override
	public boolean sendEmailOTP(String toEmail, int userId) {
		String subject = "Please submit OTP code";
		String body = "";
		try {
			Template t = templates.getTemplate("otp_email.ftl");
			Map<String, String> map = new HashMap<>();
			map.put("OTP_CODE", otp());
			body = FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
		} catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
		return sendMail(toEmail, subject, body);
	}
	
	/**
	 * Setup body, receiver and send on specific email.
	 * @param toEmail email of user
	 * @param subject subject of email
	 * @param body body of email
	 * @return true, if email was successfully sent.
	 */
	private boolean sendMail(String toEmail, String subject, String body) {
		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", mailProperties.getSmtp().getPort());
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);
			
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailProperties.getFrom(), mailProperties.getFromName()));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			msg.setSubject(subject);
			msg.setContent(body, "text/html");
			
			Transport transport = session.getTransport();
			transport.connect(mailProperties.getSmtp().getHost(),
				mailProperties.getSmtp().getUsername(), mailProperties.getSmtp().getPassword());
			transport.sendMessage(msg, msg.getAllRecipients());
			return true;
		} catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
		return false;
	}
	
	/**
	 * Generate email otp code.
	 * @return otp code
	 */
	private String otp() {
		String otp = new DecimalFormat("0000").format(new Random().nextInt(9999));
		otpCode.setOtp(otp);
		return otp;
	}
}
