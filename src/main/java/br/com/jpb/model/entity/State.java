package br.com.jpb.model.entity;

import br.com.jpb.enums.StateUf;
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
@Table(name = "state")
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
	@Column(name = "state_id")
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	@NotEmpty
	@Size(max = 75)
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Column(name = "uf")
	@NotNull
	@Enumerated(EnumType.STRING)
	public StateUf getUf() {
		return uf;
	}

	void setUf(StateUf uf) {
		this.uf = uf;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	@NotNull
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
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
		return Objects.equals(uf, other.uf) && Objects.equals(country, other.country);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}