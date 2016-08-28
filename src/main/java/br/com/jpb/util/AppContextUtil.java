/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class AppContextUtil {

	private static AppContextUtil instance;
	private ApplicationContext appContext;

	public AppContextUtil() {
	}

	public static AppContextUtil getInstance() {
		if (instance == null) {
			instance = new AppContextUtil();
		}
		return instance;
	}

	public void initialize() {
		initialize("/META-INF/applicationContext.xml");
	}

	public void initialize(final String file) {
		appContext = new ClassPathXmlApplicationContext(file);
	}

	public Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}
}