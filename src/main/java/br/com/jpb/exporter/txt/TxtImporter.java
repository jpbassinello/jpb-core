package br.com.jpb.exporter.txt;

import au.com.bytecode.opencsv.CSVReader;
import br.com.jpb.exporter.Importer;

import java.io.BufferedReader;
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

	@Override
	protected void init(InputStream is) {
		this.is = is;
	}

	@Override
	protected List<List<String>> rows(int rowsInHeader) {

		CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(is, Charset.defaultCharset())),
				separator, QUOTES, rowsInHeader);
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

		return lines.stream().map(colsArray -> Arrays.asList(colsArray)).collect(Collectors.toList());
	}

}