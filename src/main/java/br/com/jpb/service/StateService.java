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
public class StateService {

	@PersistenceContext
	private EntityManager em;

	public List<State> findAll() {
		TypedQuery<State> query = em.createQuery("SELECT s FROM State s",
				State.class);
		query.setHint(QueryHints.CACHEABLE, true);
		query.setHint(QueryHints.CACHE_REGION, "state.findAll");

		return query.getResultList();
	}

	public State findById(long id) {
		return findAll().stream().filter(state -> state.getId() == id)
				.findFirst().orElse(null);
	}
}