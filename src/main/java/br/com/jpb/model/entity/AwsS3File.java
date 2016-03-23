package br.com.jpb.model.entity;

import br.com.jpb.util.DateTimeUtil;
import br.com.jpb.util.StringUtil;
import com.google.common.io.Files;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "aws_s3_file")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
		this.hash = StringUtil.encode(String.valueOf(System.currentTimeMillis
				()), MD5_HASH_SIZE);
		this.userCreate = userCreate;
		this.createDateTime = now;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "aws_s3_file_id")
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	@Column(name = "folder")
	@NotEmpty
	@Size(max = 255)
	public String getFolder() {
		return folder;
	}

	void setFolder(String folder) {
		this.folder = folder;
	}

	@Column(name = "name")
	@NotEmpty
	@Size(max = 255)
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Column(name = "hash")
	@NotEmpty
	@Size(max = 255)
	public String getHash() {
		return hash;
	}

	void setHash(String hash) {
		this.hash = hash;
	}

	@Column(name = "extension")
	@NotEmpty
	@Size(max = 10)
	public String getExtension() {
		return extension;
	}

	void setExtension(String extension) {
		this.extension = extension;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date_time")
	@NotNull
	public Date getCreateDateTime() {
		return createDateTime;
	}

	void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	@Column(name = "user_create")
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
		return ToStringBuilder.reflectionToString(this, ToStringStyle
				.MULTI_LINE_STYLE);
	}
}
