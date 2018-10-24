package br.com.jpb.model.to;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@AllArgsConstructor
@Getter
public class UploadedImage implements Serializable {

	private String original;
	private String originalFull;
	private String thumbFull;
	private String extension;

}
