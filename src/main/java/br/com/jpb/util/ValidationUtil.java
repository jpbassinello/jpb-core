package br.com.jpb.util;

import br.com.caelum.stella.SimpleMessageProducer;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtil {

	public static final String REGEXP_EMAIL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
			"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	private static final Pattern PATTERN_EMAIL = Pattern.compile(REGEXP_EMAIL);
	private static final String VALID_CHARS_PHONE = "[\\d\\s\\-\\.\\(\\)\u00A0]+";

	public static boolean isValidCpf(String cpf) {
		try {
			CPFValidator validator = new CPFValidator(new SimpleMessageProducer(), false, false);
			validator.assertValid(cpf);
			return true;
		} catch (InvalidStateException e) {
			return false;
		}
	}

	public static boolean isValidCnpj(String cnpj) {
		try {
			CNPJValidator validator = new CNPJValidator(new SimpleMessageProducer(), false);
			validator.assertValid(cnpj);
			return true;
		} catch (InvalidStateException e) {
			return false;
		}
	}

	public static boolean isValidEmail(String email) {
		return email != null && PATTERN_EMAIL
				.matcher(email.toLowerCase())
				.matches();
	}

	public static boolean isPhoneValid(String phone) {
		return phone != null && phone.matches(VALID_CHARS_PHONE);
	}

}