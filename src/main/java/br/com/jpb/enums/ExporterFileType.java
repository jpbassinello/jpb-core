package br.com.jpb.enums;

public enum ExporterFileType {

	XLS("xls"), CSV("csv"), TXT("txt");

	private final String fileExtension;

	private ExporterFileType(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFileExtension() {
		return "." + fileExtension;
	}
}