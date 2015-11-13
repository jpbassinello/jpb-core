package br.com.jpb.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import br.com.jpb.dao.GenericDao;
import br.com.jpb.model.entity.State;

@Named
@Singleton
public class StateService {

	@Inject
	private GenericDao genericDao;

	public List<State> findAll() {
		return genericDao.findAll(State.class, true);
	}

}