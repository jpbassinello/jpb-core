package br.com.jpb.model.entity;

import br.com.jpb.util.StringUtil;
import com.google.common.io.Files;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "aws_s3_file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "hash")
@ToString
public class AwsS3File implements Serializable {

	private static final int MD5_HASH_SIZE = 8;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
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
	private LocalDateTime createDateTime;

	@Transient
	@Setter
	private File original;

	public AwsS3File(String folder, String fileName, String userCreate) {
		this.folder = folder;
		this.name = Files.getNameWithoutExtension(fileName);
		this.extension = Files.getFileExtension(fileName);
		this.hash = StringUtil.encode(String.valueOf(System.currentTimeMillis()), MD5_HASH_SIZE);
		this.userCreate = userCreate;
		this.createDateTime = LocalDateTime.now();
	}

	public String getOriginalFileName() {
		return name + "." + extension;
	}

	public String getS3FileName() {
		return hash + "." + extension;
	}

}
