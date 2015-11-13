package br.com.jpb.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.jpb.util.DateTimeUtil;
import br.com.jpb.util.ValidationUtil;

@Entity
@Table(name = "CONTACT_US")
public class ContactUs implements Serializable {

	private Long id;
	private String email;
	private String name;
	private String subject;
	private String text;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONTACT_US_ID")
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	@Column(name = "EMAIL")
	@NotEmpty(message = "Forneça um endereço de e-mail válido")
	@Size(max = 255, message = "Forneça um endereço de e-mail com no máximo {max} caracteres")
	@Pattern(regexp = ValidationUtil.REGEXP_EMAIL, message = "Forneça um endereço de e-mail válido")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "NAME")
	@NotEmpty(message = "Forneça um nome válido")
	@Size(max = 255, message = "Forneça um nome com no máximo {max} caracteres")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SUBJECT")
	@NotEmpty(message = "Forneça um assunto válido")
	@Size(max = 255, message = "Forneça um nome com no máximo {max} caracteres")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "TEXT")
	@NotEmpty(message = "Forneça uma mensagem válida")
	@Size(max = 10000, message = "Forneça uma mensagem com no máximo {max} caracteres")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEND_DATE_TIME")
	@NotNull
	public Date getSendDateTime() {
		return sendDateTime;
	}

	public void setSendDateTime(Date sendDateTime) {
		this.sendDateTime = sendDateTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ContactUs)) {
			return false;
		}
		ContactUs other = (ContactUs) obj;
		return Objects.equals(id, other.getId());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}