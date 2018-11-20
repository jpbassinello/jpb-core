package br.com.jpb.repository;

import br.com.jpb.model.entity.City;
import br.com.jpb.model.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	@QueryHints({
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION,
					value = "City.findAll")
	})
	@Override
	List<City> findAll();

	@QueryHints({
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION,
					value = "City.findByStateOrderByName")
	})
	List<City> findByStateOrderByName(State state);

	@QueryHints({
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION,
					value = "City.findByCountryAcronym")
	})
	@Query("SELECT c " +
			"FROM City c JOIN c.state s JOIN s.country co " +
			"WHERE co.acronym = :countryAcronym ORDER BY c.name")
	List<City> findByCountryAcronym(@Param("countryAcronym") String countryAcronym);

}
