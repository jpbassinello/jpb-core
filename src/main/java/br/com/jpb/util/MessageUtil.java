/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@UtilityClass
public class MessageUtil {

	public static String getString(String key, Object... args) {
		// will use default locale
		return getString(key, null, args);
	}

	public static String getString(String key, Locale locale, Object... args) {
		return messageSource().getMessage(key, args, locale);
	}

	private static MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.addBasenames("messages");
		return messageSource;
	}

}
