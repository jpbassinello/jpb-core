package br.com.jpb.exporter.txt;

import br.com.jpb.exporter.Exporter;
import com.google.common.base.Joiner;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TxtExporter<T> extends Exporter<T> {

	private static final String EMPTY = "";
	private static final char QUOTES = '"';

	private String lineSeparator = System.lineSeparator();
	private char separator = ';';
	private List<String> header;
	private Map<Integer, List<String>> lines;
	private Joiner joiner;

	public TxtExporter(Class<T> clazz) {
		super(clazz);
	}

	public TxtExporter<T> withLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
		return this;
	}

	public TxtExporter<T> withSeparator(char separator) {
		this.separator = separator;
		return this;
	}

	public TxtExporter<T> withNoHeader(boolean noHeader) {
		this.noHeader = noHeader;
		return this;
	}

	public TxtExporter<T> withIgnoredFields(Set<String> ignoredFields) {
		this.ignoredFields.addAll(ignoredFields);
		return this;
	}

	@Override
	protected void init(String fileName) {
		lines = new TreeMap<>(Comparator.naturalOrder());
		joiner = Joiner.on(separator);
	}

	@Override
	protected void initHeader() {
		header = new ArrayList<>();
	}

	@Override
	protected void writeHeaderColumn(String value, int colIndex) {
		header.add(colIndex, value);
	}

	@Override
	protected void writeEmptyColumn(int rowIndex, int colIndex) {
		getLine(rowIndex).add(EMPTY);
	}

	@Override
	protected void writeColumn(Date value, String dateFormat, int rowIndex, int colIndex) {
		writeColumn(DateTimeFormat
				.forPattern(dateFormat)
				.print(LocalDateTime.fromDateFields(value)), rowIndex, colIndex);
	}

	@Override
	protected void writeColumn(double value, int rowIndex, int colIndex) {
		getLine(rowIndex).add(String.valueOf(value));
	}

	@Override
	protected void writeColumn(int value, int rowIndex, int colIndex) {
		getLine(rowIndex).add(String.valueOf(value));
	}

	@Override
	protected void writeColumn(boolean value, int rowIndex, int colIndex) {
		getLine(rowIndex).add(Boolean
				.valueOf(value)
				.toString());
	}

	@Override
	protected void writeColumn(String value, int rowIndex, int colIndex) {
		getLine(rowIndex).add(QUOTES + value
				.replaceAll("\"", "\\\"")
				.replaceAll("\n", "") + QUOTES);
	}

	private List<String> getLine(int rowIndex) {
		return lines.computeIfAbsent(rowIndex, k -> new ArrayList<>());
	}

	@Override
	protected void writeTheFile(OutputStream os) {
		writeLine(os, header);

		for (List<String> line : lines.values()) {
			writeLine(os, line);
		}
	}

	private void writeLine(OutputStream os, List<String> line) {
		try {
			os.write(joiner
					.join(line)
					.getBytes(Charset.defaultCharset()));
			os.write(lineSeparator.getBytes(Charset.defaultCharset()));
		} catch (IOException e) {
			throw new IllegalStateException("Error while writing to the file", e);
		}
	}

	@Override
	protected void close() {
	}

}
