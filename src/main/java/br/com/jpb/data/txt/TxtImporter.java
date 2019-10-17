package br.com.jpb.data.txt;

import br.com.jpb.data.Importer;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TxtImporter<T> extends Importer<T> {

	private static final char QUOTES = '"';

	private char separator = ';';
	private InputStream is;

	public TxtImporter(Class<T> clazz) {
		super(clazz);
	}

	public TxtImporter<T> withSeparator(char separator) {
		this.separator = separator;
		return this;
	}

	public TxtImporter<T> withRowsInHeader(int rowsInHeader) {
		this.rowsInHeader = rowsInHeader;
		return this;
	}

	public TxtImporter<T> withIgnoredFields(Set<String> ignoredFields) {
		this.ignoredFields.addAll(ignoredFields);
		return this;
	}

	public static List<List<String>> readStringMatrix(File file, int rowsInHeader, char separator) {
		TxtImporter<Object> txtImporter = new TxtImporter<>(Object.class)
				.withRowsInHeader(rowsInHeader)
				.withSeparator(separator);

		try {
			txtImporter.init(new BufferedInputStream(new FileInputStream(file)));
		} catch (IOException e) {
			throw new IllegalStateException("Error while init from Input Stream", e);
		}

		return txtImporter.rows(rowsInHeader);
	}

	@Override
	protected void init(InputStream is) {
		this.is = is;
	}

	@Override
	protected List<List<String>> rows(int rowsInHeader) {

		CSVParser parser = new CSVParserBuilder()
				.withSeparator(separator)
				.withQuoteChar(QUOTES)
				.build();


		CSVReader reader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(is, Charset.defaultCharset())))
				.withCSVParser(parser)
				.withSkipLines(rowsInHeader)
				.build();

		List<String[]> lines = new ArrayList<>();
		String[] nextLine;
		try {
			while ((nextLine = reader.readNext()) != null) {
				lines.add(nextLine);
			}
			reader.close();
		} catch (IOException e) {
			throw new IllegalStateException("Error while reading TXT", e);
		}

		return lines
				.stream()
				.map(Arrays::asList)
				.collect(Collectors.toList());
	}

}