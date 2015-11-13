package br.com.jpb.charts;

import java.util.stream.Stream;

public abstract class JsonChart {

	public abstract String getColors();

	public abstract String getData();

	public abstract String getDataFormat();

	protected String[] singleQuote(String... s) {
		return Stream.of(s).map(str -> singleQuote(str)).toArray(String[]::new);
	}

	protected String singleQuote(String s) {
		return "'" + s + "'";
	}
}