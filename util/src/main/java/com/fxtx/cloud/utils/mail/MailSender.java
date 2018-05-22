package com.fxtx.cloud.utils.mail;

import java.util.List;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	/**
	 * 发送邮件的props文件
	 */
	private final transient Properties props = System.getProperties();
	private transient MailAuthenticator auth;
	private transient Session session;

	public MailSender(final String host, final String username, final String password){
		init(host, username, password);
	}
	
	public MailSender(final String mail, final String password){
		final String host = "smtp." + mail.split("@")[1];
		init(host, mail, password);
	}
	
	private void init(String host, String username, String password) {
		// 初始化props
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", host);
	
		// 验证
		auth = new MailAuthenticator(username, password);
		
		// 创建session
		session = Session.getInstance(props, auth);
	}
	
	public void send(String recipient, String subject, Object content)
			throws AddressException, MessagingException {
	
	    // 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
	
		// 设置发信人
		message.setFrom(new InternetAddress(auth.getUsername()));
		
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		
		// 设置主题
		message.setSubject(subject);
		
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		
		// 发送
		Transport.send(message);
	}
	
    public void send(List<String> recipients, String subject, Object content)
		throws AddressException, MessagingException {
		
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		
		// 设置发信人
		message.setFrom(new InternetAddress(auth.getUsername()));
		
		// 设置收件人们
		final int num = recipients.size();
		InternetAddress[] addresses = new InternetAddress[num];
		
		for (int i = 0; i < num; i++) {
		    addresses[i] = new InternetAddress(recipients.get(i));
		}
		message.setRecipients(RecipientType.TO, addresses);
		
		// 设置主题
		message.setSubject(subject);
		
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		
		// 发送
		Transport.send(message);
		
	}
}
