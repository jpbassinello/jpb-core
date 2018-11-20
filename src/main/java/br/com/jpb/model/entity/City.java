package br.com.jpb.model.entity;

import br.com.jpb.util.StringUtil;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "city")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {
		"name",
		"state"
})
@ToString
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Immutable
public class City implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	@NotEmpty
	@Size(max = 100)
	private String name;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private State state;

	@Transient
	private String nameDashStateAcronym;
	@Transient
	private String searchNameDashStateAcronym;

	public City(String name, State state) {
		this.name = name;
		this.state = state;
	}

	@PostLoad
	public void postLoad() {
		this.nameDashStateAcronym = this.name + " - " + this.state.getAcronym();
		this.searchNameDashStateAcronym = StringUtil
				.removeAccents(this.nameDashStateAcronym)
				.toUpperCase();
	}

	public boolean checkSearch(String search) {
		return this.searchNameDashStateAcronym.contains(StringUtil
				.removeAccents(search)
				.toUpperCase());
	}
}
