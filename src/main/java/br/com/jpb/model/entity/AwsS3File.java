package br.com.jpb.model.entity;

import br.com.jpb.model.BaseEntity;
import br.com.jpb.util.DateTimeUtil;
import br.com.jpb.util.StringUtil;
import com.google.common.io.Files;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.util.Date;

@Entity
@Table(name = "aws_s3_file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "hash")
@ToString
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AwsS3File implements BaseEntity {

	private static final int MD5_HASH_SIZE = 8;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "aws_s3_file_id")
	private Long id;

	@Column(name = "folder")
	@NotEmpty
	@Size(max = 255)
	private String folder;

	@Column(name = "name")
	@NotEmpty
	@Size(max = 255)
	private String name;

	@Column(name = "hash")
	@NotEmpty
	@Size(max = 255)
	private String hash;

	@Column(name = "extension")
	@NotEmpty
	@Size(max = 10)
	private String extension;

	@Column(name = "user_create")
	@NotEmpty
	@Size(max = 100)
	private String userCreate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date_time")
	@NotNull
	private Date createDateTime;

	@Transient
	@Setter
	private File original;

	public AwsS3File(String folder, String fileName, String userCreate) {
		Date now = DateTimeUtil.nowWithDateTimeInUTC().toDate();
		this.folder = folder;
		this.name = Files.getNameWithoutExtension(fileName);
		this.extension = Files.getFileExtension(fileName);
		this.hash = StringUtil.encode(String.valueOf(System.currentTimeMillis()), MD5_HASH_SIZE);
		this.userCreate = userCreate;
		this.createDateTime = now;
	}

	public String getOriginalFileName() {
		return name + "." + extension;
	}

	public String getS3FileName() {
		return hash + "." + extension;
	}

}
