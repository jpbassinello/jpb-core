package br.com.jpb.model.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.jpb.enums.StateUf;

@Entity
@Table(name = "STATE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
public class State implements Serializable {

	private Long id;
	private String name;
	private StateUf uf;
	private Country country;

	protected State() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STATE_ID")
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	@NotEmpty
	@Size(max = 75)
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Column(name = "UF")
	@NotNull
	@Enumerated(EnumType.STRING)
	public StateUf getUf() {
		return uf;
	}

	void setUf(StateUf uf) {
		this.uf = uf;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COUNTRY_ID")
	@NotNull
	public Country getCountry() {
		return country;
	}

	void setCountry(Country country) {
		this.country = country;
	}

	public int hashCode() {
		return Objects.hash(uf, country);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof State)) {
			return false;
		}
		State other = (State) obj;
		return Objects.equals(uf, other.uf)
				&& Objects.equals(country, other.country);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}