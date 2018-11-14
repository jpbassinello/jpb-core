package br.com.jpb.repository;

import br.com.jpb.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	@QueryHints({
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION,
					value = "Country.findAll")
	})
	@Override
	List<Country> findAll();

	@QueryHints({
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"),
			@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION,
					value = "Country.findByAcronymOrderByName")
	})
	Country findByAcronymOrderByName(String acronym);

}
