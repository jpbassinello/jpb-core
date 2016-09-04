package br.com.jpb.service;

import br.com.jpb.enums.StateUf;
import br.com.jpb.model.entity.City;
import br.com.jpb.model.entity.State;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class CityService extends GenericService<City> {

	@Override
	public List<City> findAll() {
		return createJPAQuery("city.findAll").fetch();
	}

	public List<City> findByState(final State state) {
		return findByState(state.getUf());
	}

	public List<City> findByState(final StateUf uf) {
		return findAll().stream().filter(city -> city.getState().getUf() == uf).collect(Collectors.toList());
	}

	@Override
	public City findById(long id) {
		return findAll().stream().filter(city -> city.getId() == id).findFirst().orElse(null);
	}
}