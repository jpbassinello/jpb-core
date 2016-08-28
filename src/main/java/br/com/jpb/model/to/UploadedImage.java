package br.com.jpb.model.to;

import java.io.Serializable;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class UploadedImage implements Serializable {

	private String original;
	private String originalFull;
	private String thumbFull;
	private String extension;

	public UploadedImage(String original, String originalFull, String thumbFull, String extension) {
		this.original = original;
		this.originalFull = originalFull;
		this.thumbFull = thumbFull;
		this.extension = extension;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getOriginalFull() {
		return originalFull;
	}

	public void setOriginalFull(String originalFull) {
		this.originalFull = originalFull;
	}

	public String getThumbFull() {
		return thumbFull;
	}

	public void setThumbFull(String thumbFull) {
		this.thumbFull = thumbFull;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}