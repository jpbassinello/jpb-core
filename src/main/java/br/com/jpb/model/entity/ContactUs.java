package br.com.jpb.model.entity;

import br.com.jpb.util.ValidationUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_us")
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class ContactUs implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "email")
	@NotEmpty(message = "{contactUs.email.notEmpty}")
	@Size(max = 255, message = "{contactUs.email.size}")
	@Pattern(regexp = ValidationUtil.REGEXP_EMAIL, message = "{contactUs.email.pattern}")
	@Setter
	private String email;

	@Column(name = "name")
	@NotEmpty(message = "{contactUs.name.notEmpty}")
	@Size(max = 255, message = "{contactUs.name.size}")
	@Setter
	private String name;

	@Column(name = "subject")
	@NotEmpty(message = "{contactUs.subject.notEmpty}")
	@Size(max = 255, message = "{contactUs.subject.size")
	@Setter
	private String subject;

	@Column(name = "text")
	@NotEmpty(message = "{contactUs.text.notEmpty}")
	@Size(max = 50_000, message = "{contactUs.text.size}")
	@Setter
	private String text;

	@Column(name = "SEND_DATE_TIME")
	@NotNull(message = "{contactUs.sendDateTime.notEmpty}")
	@Setter
	private LocalDateTime sendDateTime;

	public ContactUs() {
	}

	public ContactUs(String email, String name, String subject, String text) {
		this.sendDateTime = LocalDateTime.now();
		this.email = email;
		this.name = name;
		this.subject = subject;
		this.text = text;
	}

}
