package br.com.jpb.model.entity;

import br.com.jpb.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "city")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"name", "state"})
@ToString
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
public class City implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "city_id")
	private Long id;

	@Column(name = "name")
	@NotEmpty
	@Size(max = 75)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private State state;
}