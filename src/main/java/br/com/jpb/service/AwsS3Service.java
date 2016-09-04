package br.com.jpb.service;

import br.com.jpb.model.entity.AwsS3File;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

@Named
@Singleton
public class AwsS3Service extends GenericService<AwsS3File> {

	private static final int NUMBER_OF_TRIES_TO_PUT_OBJECT = 3;
	private final ResponseHeaderOverrides headerContentDispositionAttachment = new ResponseHeaderOverrides()
			.withContentDisposition("attachment");
	private
	@Value("${aws.access.key}")
	String ACCESS_KEY;
	private
	@Value("${aws.secret.access.key}")
	String SECRET_KEY;
	private
	@Value("${aws.s3.bucket}")
	String BUCKET;
	private
	@Value("${aws.s3.endpoint}")
	String ENDPOINT;
	private
	@Value("${aws.s3.baseFolder}")
	String BASE_FOLDER;
	private AmazonS3 amazonS3;

	@PostConstruct
	public void init() {
		AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		amazonS3 = new AmazonS3Client(credentials);
		amazonS3.setEndpoint(ENDPOINT);
	}

	public void delete(AwsS3File file) {
		amazonS3.deleteObject(BUCKET, BASE_FOLDER + "/" + file.getFolder() + "/" + file.getS3FileName());
	}

	public AwsS3File create(File file, String folder, String userCreate) {
		final ObjectMetadata om = new ObjectMetadata();
		om.setContentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file));
		om.setContentLength(file.length());

		AwsS3File awsS3File = new AwsS3File(folder, file.getName(), userCreate);

		for (int i = 0; i < NUMBER_OF_TRIES_TO_PUT_OBJECT; i++) {
			try {
				amazonS3.putObject(BUCKET, BASE_FOLDER + "/" + folder + "/" + awsS3File.getS3FileName(),
						new BufferedInputStream(new FileInputStream(file)), om);
				break;
			} catch (AmazonServiceException e) {
				continue;
			} catch (FileNotFoundException e) {
				throw new IllegalStateException("IO Exception while create FileInputStream", e);
			}
		}

		return awsS3File;
	}

	public URL getAwsS3FileURL(AwsS3File file, boolean isDownload) {
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(BUCKET,
				BASE_FOLDER + "/" + file.getFolder() + "/" + file.getS3FileName());

		if (isDownload) {
			return amazonS3.generatePresignedUrl(request.withResponseHeaders(headerContentDispositionAttachment));
		}

		return amazonS3.generatePresignedUrl(request);
	}

	public URL getAwsS3FileURL(AwsS3File file, LocalDateTime expiresIn, boolean isDownload) {
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(BUCKET,
				BASE_FOLDER + "/" + file.getFolder() + "/" + file.getS3FileName());

		request.setExpiration(expiresIn.toDate());

		if (isDownload) {
			return amazonS3.generatePresignedUrl(request.withResponseHeaders(headerContentDispositionAttachment));
		}

		return amazonS3.generatePresignedUrl(request);
	}
}
