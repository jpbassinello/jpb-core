package br.com.jpb.model.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "city")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
public class City implements Serializable {

	private Long id;
	private String name;
	private State state;

	protected City() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "city_id")
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public State getState() {
		return state;
	}

	void setState(State state) {
		this.state = state;
	}

	public int hashCode() {
		return Objects.hash(name, state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof City)) {
			return false;
		}
		City other = (City) obj;
		return Objects.equals(name, other.name) && Objects.equals(state, other.state);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}