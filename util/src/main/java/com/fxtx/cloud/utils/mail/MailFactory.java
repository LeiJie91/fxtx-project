package com.fxtx.cloud.utils.mail;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class MailFactory implements ResourceLoaderAware, InitializingBean, DisposableBean {
	private MailSender sender = null;
	private String host = "smtp.exmail.qq.com";
	private String username="customer_service@hwyf123.com";
	private String password="XiaoYou123_BJcq!@#";


	
	public void setHost(String host) {
		this.host = host;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MailSender getSender() {
		return sender;
	}

	public void destroy() throws Exception {
		this.sender = null;
	}

	public void afterPropertiesSet() throws Exception {
		this.sender = new MailSender(host, username, password);
	}
	//mailFactory.getSender().send(
	public void setResourceLoader(ResourceLoader arg0) {
		// TODO Auto-generated method stub
		
	}
}
