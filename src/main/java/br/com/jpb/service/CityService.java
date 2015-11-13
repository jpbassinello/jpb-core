package br.com.jpb.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.jpb.enums.StateUf;
import br.com.jpb.model.entity.City;
import br.com.jpb.model.entity.State;

@Named
@Singleton
public class CityService {

	@PersistenceContext
	private EntityManager em;

	public List<City> findAll() {
		TypedQuery<City> query = em.createQuery(
				"SELECT c FROM City c JOIN FETCH c.state", City.class);
		query.setHint("org.hibernate.cacheable", true);

		return query.getResultList();
	}

	public List<City> findByState(final State state) {
		return findByState(state.getUf());
	}

	public List<City> findByState(final StateUf uf) {
		return findAll().stream().filter(city -> city.getState().getUf() == uf)
				.collect(Collectors.toList());
	}

}