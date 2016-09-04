package br.com.jpb.service;

import br.com.jpb.model.entity.State;
import org.hibernate.annotations.QueryHints;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Named
@Singleton
public class StateService extends GenericService<State> {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<State> findAll() {
		return createJPAQuery("state.findAll").fetch();
	}

	@Override
	public State findById(long id) {
		return findAll().stream().filter(state -> state.getId() == id).findFirst().orElse(null);
	}
}