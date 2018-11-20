package br.com.jpb.model.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "state")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {
		"acronym",
		"country"
})
@ToString
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class State implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	@NotEmpty
	@Size(max = 100)
	private String name;

	@Column(name = "acronym")
	@NotEmpty
	@Size(max = 10)
	private String acronym;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	@NotNull
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Country country;

	public State(String name, String acronym, Country country) {
		this.name = name;
		this.acronym = acronym;
		this.country = country;
	}
}