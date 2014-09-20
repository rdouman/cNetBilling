package org.communinet.billing.impl.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AxxessNotificationAdapter implements INotificationAdapter {

	private String configFilename = "config/smtp.properties";
	private Session session;
	private String axxessUsername="dou17";
	private String axxessPassword="dytqt";
	
	static final Logger logger = LoggerFactory
			.getLogger(AxxessNotificationAdapter.class);

	public AxxessNotificationAdapter() {
		
		final String username = "rdouman@gmail.com";
		final String password = "d8gmail2ay";
		
		session = getSmtpSession(username, password);
	}
	
	public void notifyCustomer(String mobileNumber, String message) {
		sendNotification(mobileNumber+"@sms.axxess.co.za", "admin@communinet.co.za", axxessUsername+":"+axxessPassword, message);
	}

	private void sendNotification(String toEmail, String fromEmail,
			String subject, String content) {

		try {

			logger.debug("Sending email with params: {}, {}, {},{}", new String[]{toEmail, fromEmail, subject, content});
			
			logger.info("Sending notification {} to {}", content, toEmail);
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEmail));
			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);

			logger.info("Sent notification {} to {}", content, toEmail);

		} catch (MessagingException e) {
			logger.error("An error occurredbytesUsed whilst send sms notification");
			throw new RuntimeException(e);
		}
	}

	private Session getSmtpSession(final String username, final String password) {

		Properties props = loadProperties(new File(configFilename));
	
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		return session;
	}

	private Properties loadProperties(File f) {
		
		logger.info("Loading properties file {}",f.getAbsolutePath());
		Properties pro = new Properties();
		if (f.exists()) {

			FileInputStream in;
			try {
				in = new FileInputStream(f);
				pro.load(in);
			} catch (IOException e) {
				logger.error("An error occured while loading {}, {}", f,e );
			}

		}
		
		logger.info("Done load properties file {}", f.getAbsolutePath());
		return pro;
	}

	public static void main(String[] args) {
		AxxessNotificationAdapter test = new AxxessNotificationAdapter();
		//test.sendNotification("support@communinet.co.za", "rdouman@gmail.com", "Yet another test support", "Woohoo this works");
		//test.sendNotification("admin@communinet.co.za", "rdouman@gmail.com", "Yet another test admin", "Woohoo this works");
		test.notifyCustomer("+27794808622", "This is a test");
		
		//test.sendNotification("info@communinet.co.za", "admin@communinet.co.za", "Yet another test info", "Woohoo this works");
	}
	// public void updateNot
}
