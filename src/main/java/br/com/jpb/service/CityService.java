package br.com.jpb.service;

import br.com.jpb.enums.StateUf;
import br.com.jpb.model.entity.City;
import br.com.jpb.model.entity.State;
import org.hibernate.annotations.QueryHints;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class CityService {

	@PersistenceContext
	private EntityManager em;

	public List<City> findAll() {
		TypedQuery<City> query = em.createQuery("SELECT c FROM City c", City.class);
		query.setHint(QueryHints.CACHEABLE, true);
		query.setHint(QueryHints.CACHE_REGION, "city.findAll");

		return query.getResultList();
	}

	public List<City> findByState(final State state) {
		return findByState(state.getUf());
	}

	public List<City> findByState(final StateUf uf) {
		return findAll().stream().filter(city -> city.getState().getUf() == uf).collect(Collectors.toList());
	}

	public City findById(long id) {
		return findAll().stream().filter(city -> city.getId() == id).findFirst().orElse(null);
	}
}