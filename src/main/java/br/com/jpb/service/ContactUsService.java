package br.com.jpb.service;

import br.com.jpb.dao.GenericDao;
import br.com.jpb.model.entity.ContactUs;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ContactUsService {

	@Inject
	private transient GenericDao genericDao;

	@Transactional
	public void save(ContactUs contactUs) {
		genericDao.merge(contactUs);
	}
}
