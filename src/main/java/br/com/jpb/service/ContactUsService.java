package br.com.jpb.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.joda.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;

import br.com.jpb.dao.GenericDao;
import br.com.jpb.exception.FailedToSendEmailException;
import br.com.jpb.model.entity.ContactUs;
import br.com.jpb.util.DateTimeUtil;

@Named
@Singleton
public class ContactUsService {

	@Inject
	private transient GenericDao genericDao;

	@Inject
	private transient MandrillMailService mandrillMailService;

	@Transactional
	public void saveContactAndSendEmail(ContactUs contactUs, String emailTo,
			String nameTo) throws FailedToSendEmailException {
		genericDao.merge(contactUs);
		sendContactUsEmail(contactUs, emailTo, nameTo);
	}

	public void sendContactUsEmail(ContactUs contactUs, String emailTo,
			String nameTo) throws FailedToSendEmailException {

		String subject = "[CONTATO CTRL/S VIA FERRAMENTA] "
				+ contactUs.getSubject();

		String htmlBody = Joiner
				.on("<br />")
				.join("De: " + contactUs.getName() + " - "
						+ contactUs.getEmail(),
						"Data/Hora: "
								+ DateTimeUtil
										.getDefaultViewFormattedLocalDateTime(LocalDateTime
												.now()) + " UTC", "", "",
						"Texto: " + contactUs.getText());

		mandrillMailService.sendMessage(contactUs.getEmail(),
				contactUs.getName(), new String[] { emailTo },
				new String[] { nameTo },
				subject, htmlBody);
	}
}
