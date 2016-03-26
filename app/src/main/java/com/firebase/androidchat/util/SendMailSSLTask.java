package com.firebase.androidchat.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class SendMailSSLTask extends AsyncTask<String, String, String> {
	protected static final int USERNAME = 0, PASSWORD = 1, SENDTO = 2,SUBJECT = 3, HOST = 4, PORT = 5, CONTENT = 6;
	private final Activity sendMailActivity;
	private ProgressDialog statusDialog;

	public SendMailSSLTask(Activity activity) {
		sendMailActivity = activity;
	}
    @Override
    protected void onPreExecute() {
		statusDialog = new ProgressDialog(sendMailActivity);
		statusDialog.setMessage("Getting ready...");
		statusDialog.setIndeterminate(false);
		statusDialog.setCancelable(false);
		statusDialog.show();
    }

    @Override
    protected String doInBackground(final String... formFieldValues) {
		publishProgress("Processing input....");
    	final String username = formFieldValues[USERNAME];
    	final String password = formFieldValues[PASSWORD];
    	Properties props = new Properties();
		props.put("mail.smtp.host", formFieldValues[HOST]);
		props.put("mail.smtp.socketFactory.port", formFieldValues[PORT]);// 465 for gmail
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", formFieldValues[PORT]);
 
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username,password);
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress(formFieldValues[0]));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(formFieldValues[SENDTO]));
			message.setSubject(formFieldValues[SUBJECT]);
//			message.setText(formFieldValues[4]);
			message.setContent(formFieldValues[CONTENT], "text/html; charset=utf-8");
 
            //Use Transport to deliver the message
            Transport transport = session.getTransport("smtp");
            transport.connect(formFieldValues[HOST], username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
			publishProgress("Email Sent.");
			return ("Done");
 
		} catch (MessagingException e) {
			publishProgress(e.getMessage());
			throw new RuntimeException(e);
		}
    }

	@Override
	public void onProgressUpdate(String... values) {
		statusDialog.setMessage(values[0].toString());

	}

    @Override
    protected void onPostExecute(String result) {
		statusDialog.dismiss();
    }
}