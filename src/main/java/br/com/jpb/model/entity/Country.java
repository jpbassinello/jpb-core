package br.com.jpb.model.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.jpb.enums.CountryAcronym;

@Entity
@Table(name = "COUNTRY")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Country implements Serializable {

	private Long id;
	private String name;
	private CountryAcronym acronym;

	protected Country() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COUNTRY_ID")
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	@NotEmpty
	@Size(max = 50)
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Column(name = "ACRONYM")
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
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}