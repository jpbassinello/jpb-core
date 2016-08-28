package br.com.jpb.model.entity;

import br.com.jpb.enums.CountryAcronym;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "country")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
public class Country implements Serializable {

	private Long id;
	private String name;
	private CountryAcronym acronym;

	protected Country() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "country_id")
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	@NotEmpty
	@Size(max = 50)
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Column(name = "acronym")
	@NotNull
	@Enumerated(EnumType.STRING)
	public CountryAcronym getAcronym() {
		return acronym;
	}

	void setAcronym(CountryAcronym acronym) {
		this.acronym = acronym;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acronym);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Country)) {
			return false;
		}
		Country other = (Country) obj;
		return Objects.equals(acronym, other.acronym);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}