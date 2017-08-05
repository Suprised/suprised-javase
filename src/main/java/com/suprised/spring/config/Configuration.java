package com.suprised.spring.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.suprised.spring.SpringServiceHelper;

/**
 * @author y
 *
 */
@Component
public class Configuration implements ApplicationContextAware {

	@Value("${jdbc.driver}")
	public String jdbc_driver;
	
	@Value("${jdbc.url}")
	public String jdbc_url;
	
	@Value("${jdbc.username}")
	public String jdbc_username;
	
	@Value("${jdbc.password}")
	public String jdbc_password;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext application)
			throws BeansException {
		SpringServiceHelper.setApplicationContext(application);
	}
	
	public static Configuration getConfigInstance() {
		return SpringServiceHelper.getBean(Configuration.class);
	}

	public String getDriver() {
		return this.jdbc_driver;
	}
}
