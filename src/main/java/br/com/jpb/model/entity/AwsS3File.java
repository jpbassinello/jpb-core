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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.jpb.util.DateTimeUtil;
import br.com.jpb.util.StringUtil;

import com.google.common.io.Files;

@Entity
@Table(name = "AWS_S3_FILE")
public class AwsS3File implements Serializable {

	private static final int MD5_HASH_SIZE = 8;

	private Long id;
	private String folder;
	private String name;
	private String hash;
	private String extension;
	private String userCreate;
	private Date createDateTime;

	protected AwsS3File() {
	}

	public AwsS3File(String folder, String fileName, String userCreate) {
		Date now = DateTimeUtil.nowWithDateTimeInUTC().toDate();
		this.folder = folder;
		this.name = Files.getNameWithoutExtension(fileName);
		this.extension = Files.getFileExtension(fileName);
		this.hash = StringUtil.encode(
				String.valueOf(System.currentTimeMillis()), MD5_HASH_SIZE);
		this.userCreate = userCreate;
		this.createDateTime = now;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AWS_S3_FILE_ID")
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FOLDER")
	@NotEmpty
	@Size(max = 255)
	public String getFolder() {
		return folder;
	}
	
	void setFolder(String folder) {
		this.folder = folder;
	}
	
	@Column(name = "NAME")
	@NotEmpty
	@Size(max = 255)
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Column(name = "HASH")
	@NotEmpty
	@Size(max = 255)
	public String getHash() {
		return hash;
	}

	void setHash(String hash) {
		this.hash = hash;
	}

	@Column(name = "EXTENSION")
	@NotEmpty
	@Size(max = 10)
	public String getExtension() {
		return extension;
	}

	void setExtension(String extension) {
		this.extension = extension;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE_TIME")
	@NotNull
	public Date getCreateDateTime() {
		return createDateTime;
	}

	void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	@Column(name = "USER_CREATE")
	@NotEmpty
	@Size(max = 100)
	public String getUserCreate() {
		return userCreate;
	}

	void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}

	@Transient
	public String getOriginalFileName() {
		return name + "." + extension;
	}

	@Transient
	public String getS3FileName() {
		return hash + "." + extension;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hash);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AwsS3File)) {
			return false;
		}
		AwsS3File other = (AwsS3File) obj;
		return Objects.equals(hash, other.getHash());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
