package com.sunny.Mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.Toast;

import com.sun.mail.smtp.SMTPSSLTransport;

/**
 * @author spasnoor
 * 
 */
@SuppressLint("NewApi")
public class SendMail {
    private MimeMessage msg;
    private Session session;
    private String host = "smtp.gmail.com";
    private String userid = "";
    private String password = "";
    private List<String> toEmails;
    private Handler handler;

    /**
     * @param fromUserId
     * @param fromPassword
     * @param toUserName
     */
    public void setParameters(String fromUserId, String fromPassword,
	    String toUserName) {
	toEmails = new ArrayList<String>();
	userid = fromUserId;
	password = fromPassword;
	toEmails.add(toUserName);
    }

    /**
     * @param ctx
     * @param mailContentToSend
     */
    public void composeMail(final Context ctx, final String mailContentToSend) {
	handler = new Handler();
	handler.post(new Runnable() {

	    @Override
	    public void run() {
		try {
		  

		    String enable = "true";
		    Properties props = System.getProperties();
		    props.put("mail.smtp.starttls.enable", enable);
		    props.put("mail.smtp.host", "smtp.rediffmail.com");
		    props.setProperty("mail.transport.protocol", "smptp");
		    props.put("mail.smtp.user", userid);
		    props.put("mail.smtp.password", password);
		    props.put("mail.smtp.port", "25");
		    props.put("mail.smtps.auth", "true");
		    session = Session.getDefaultInstance(props, null);
		    msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress(userid));

		    for (String email : toEmails) {
			msg.addRecipient(Message.RecipientType.TO,
				new InternetAddress(email));
		    }
		    msg.setSubject("User data is ");

		 
		    msg.setContent(mailContentToSend, "text/html");
		    SMTPSSLTransport transport = (SMTPSSLTransport) session
			    .getTransport("smtps");
		    transport.connect(host, userid, password);
		    transport.sendMessage(msg, msg.getAllRecipients());
		    transport.close();
		    handler.post(new Runnable() {

			@Override
			public void run() {
			    Toast.makeText(ctx,
				    "SENT MAIL TO  " + toEmails.get(0),
				    Toast.LENGTH_LONG).show();

			}
		    });

		} catch (Exception e) {
		    e.printStackTrace();
		}

	    }
	});

    }

}
