package com.suprised.spring;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring管理bean的生命周期
 */
@Component
public class LifeBean implements BeanFactoryAware , ApplicationContextAware , BeanPostProcessor, InitializingBean {

	@Value("${LifeBean.name}")
	private String name;
	
	@Value("${LifeBean.password}")
	private String password;
	
	@Value("${LifeBean.age}")
	private int age;
	
	@Autowired
	private WritedBean writeBean;
	
	@Autowired
	private WritedBean writeBean2;
	
	@Autowired
	@Qualifier("writedBeanImpl")
	private WritedBeanInterface impl1;

	@Autowired
	@Qualifier("writedBeanImpl2")
	private WritedBeanInterface impl2;
	
	public LifeBean() {
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("注入BeanFactory");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		System.out.println("注入ApplicationContext");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("afterPropertiesSet()..");
	}
	
	@PostConstruct
	public void initMethod() {
		System.out.println("initMethod()...");
	}
	
	@Override
	public Object postProcessBeforeInitialization(Object obj, String str)
			throws BeansException {
		System.out.println("postProcessBeforeInitialization(); param1: " + obj + ", param2: " + str);
		return obj;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object obj, String str)
			throws BeansException {
		System.out.println("postProcessBeforeInitialization(); param1: " + obj + ", param2: " + str);
		return obj;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public WritedBean getWriteBean() {
		return writeBean;
	}

	public void setWriteBean(WritedBean writeBean) {
		this.writeBean = writeBean;
	}

	public WritedBean getWriteBean2() {
		return writeBean2;
	}

	public void setWriteBean2(WritedBean writeBean2) {
		this.writeBean2 = writeBean2;
	}

	public WritedBeanInterface getImpl1() {
		return impl1;
	}

	public void setImpl1(WritedBeanInterface impl1) {
		this.impl1 = impl1;
	}

	public WritedBeanInterface getImpl2() {
		return impl2;
	}

	public void setImpl2(WritedBeanInterface impl2) {
		this.impl2 = impl2;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
