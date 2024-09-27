package utilities;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;

public class EmailOps {

	static CommonUtilities commonUtils = new CommonUtilities();
	static Multipart multipart = null;
	public static void sendEmail(String emailContent, String outPutFile, String extReportFile) throws IOException {
		String emailTo = null;
		Properties props = new Properties();
		final String username = commonUtils.getProperties("emailSender");
		final String password = commonUtils.getProperties("senderPassword");
		
		props.put("mail.smtp.auth", "true"); // Enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // Enable STATTLS
		props.put("mail.smtp.host", "subaru-com.mail.protection.outlook.com");
		props.put("mail.smtp.port", "25");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			emailTo = commonUtils.getProperties("recipientsList");
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
			message.setSubject("Telematics Regression Suite - Test Results");
			
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(emailContent, "text/html; charset=utf-8");
			multipart = new MimeMultipart("mixed");
			addAttachment(outPutFile);
			addAttachment(extReportFile);
			multipart.addBodyPart(textPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void addAttachment(String outPutFile) throws MessagingException {
		String Filename = null;
		DataSource source = new FileDataSource(outPutFile);
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(new DataHandler(source));
		Filename = commonUtils.getFileNameFromPath(outPutFile);
		messageBodyPart.setFileName(Filename);
		multipart.addBodyPart(messageBodyPart);
	}

	@SuppressWarnings("unused")
	public static StringBuilder emailContent(String telematicsServiceName,String testEnvironment,String finalResult) {
		StringBuilder EmailBody = new StringBuilder();
		String emailContent = "ContentNotAppended";
		if (!StringUtils.contains("PASS", finalResult)){
		EmailBody
		.append("<table border=\"1\"layout: fixed style=\"width:600px\"><tr><td layout: fixed style=\"width:200px\" align=\"left\">"
				+ telematicsServiceName
				+ "</td><td layout: fixed style=\"width:200px\" align=\"center\">"
				+ testEnvironment
				+ "</td><td layout: fixed style=\"width:200px\" style=\"color:red\">"+finalResult+"</td></tr></table>");
		} else {
			EmailBody
			.append("<table border=\"1\"layout: fixed style=\"width:600px\"><tr><td layout: fixed style=\"width:200px\" align=\"left\">"
					+ telematicsServiceName
					+ "</td><td layout: fixed style=\"width:200px\" align=\"center\">"
					+ testEnvironment
					+ "</td><td layout: fixed style=\"width:200px\" style=\"color:green\">"+finalResult+"</td></tr></table>");
		}
		emailContent = EmailBody.toString();
		return EmailBody;
	}
}