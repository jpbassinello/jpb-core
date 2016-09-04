package br.com.jpb.model.entity;

import br.com.jpb.model.BaseEntity;
import br.com.jpb.util.DateTimeUtil;
import br.com.jpb.util.ValidationUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "CONTACT_US")
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class ContactUs implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONTACT_US_ID")
	private Long id;

	@Column(name = "EMAIL")
	@NotEmpty(message = "Forneça um endereço de e-mail válido")
	@Size(max = 255, message = "Forneça um endereço de e-mail com no máximo " + "{max} caracteres")
	@Pattern(regexp = ValidationUtil.REGEXP_EMAIL, message = "Forneça um " + "endereço de e-mail válido")
	@Setter
	private String email;

	@Column(name = "NAME")
	@NotEmpty(message = "Forneça um nome válido")
	@Size(max = 255, message = "Forneça um nome com no máximo {max} " + "caracteres")
	@Setter
	private String name;

	@Column(name = "SUBJECT")
	@NotEmpty(message = "Forneça um assunto válido")
	@Size(max = 255, message = "Forneça um assunto com no máximo {max} " + "caracteres")
	@Setter
	private String subject;

	@Column(name = "TEXT")
	@NotEmpty(message = "Forneça uma mensagem válida")
	@Size(max = 10000, message = "Forneça uma mensagem com no máximo {max} " + "caracteres")
	@Setter
	private String text;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEND_DATE_TIME")
	@NotNull
	@Setter
	private Date sendDateTime;

	public ContactUs() {
		this.sendDateTime = DateTimeUtil.nowWithDateTimeInUTC().toDate();
	}

	public ContactUs(String email, String name, String subject, String text) {
		this();
		this.email = email;
		this.name = name;
		this.subject = subject;
		this.text = text;
	}

}