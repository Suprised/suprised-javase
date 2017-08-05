package com.suprised.spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.suprised.spring.config.Configuration;

@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class TestBase extends AbstractJUnit4SpringContextTests {

	@Autowired
	private LifeBean lifeBean;
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private Configuration config2;
	
	@Test
	public void testLifeBean() {
		System.out.println(lifeBean);
	}
	
	@Test
	public void testConfiguration() {
		System.out.println(configuration);
		System.out.println(config2);
	}
	
	@Test
	public void testConfig() {
		System.out.println(Configuration.getConfigInstance().getDriver());
	}
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		LifeBean bean = applicationContext.getBean(LifeBean.class);
		Configuration configBean = applicationContext.getBean(Configuration.class);
		System.out.println(configBean);
		System.out.println(bean);
	}
}
