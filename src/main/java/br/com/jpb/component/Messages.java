package br.com.jpb.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class Messages {

	private static MessageSource messageSource;

	@Autowired
	Messages(MessageSource messageSource) {
		Messages.messageSource = messageSource;
	}

	public static String getMessage(String bundleKey, Object... args) {
		if (messageSource == null) {
			ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
			messageSource.setBasenames("jpb-core-messages");
			Messages.messageSource = messageSource;
		}

		return messageSource.getMessage(bundleKey, args, LocaleContextHolder.getLocale());
	}

}
