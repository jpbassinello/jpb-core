package br.com.jpb.repository;

import br.com.jpb.model.entity.Country;
import br.com.jpb.model.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

	@QueryHints({
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION,
					value = "State.findAll")
	})
	@Override
	List<State> findAll();

	@QueryHints({
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION,
					value = "State.findByCountryOrderByName")
	})
	List<State> findByCountryOrderByName(Country country);

	List<State> findByAcronymAndCountryOrderByName(String acronym, Country country);

}
