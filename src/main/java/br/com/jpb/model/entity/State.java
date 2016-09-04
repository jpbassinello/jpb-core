package br.com.jpb.model.entity;

import br.com.jpb.enums.StateUf;
import br.com.jpb.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "state")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"uf", "country"})
@ToString
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
public class State implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "state_id")
	private Long id;

	@Column(name = "name")
	@NotEmpty
	@Size(max = 75)
	private String name;

	@Column(name = "uf")
	@NotNull
	@Enumerated(EnumType.STRING)
	private StateUf uf;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	@NotNull
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private Country country;

}