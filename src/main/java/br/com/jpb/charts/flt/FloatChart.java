package br.com.jpb.charts.flt;

import br.com.jpb.charts.JsonChart;

public abstract class FloatChart extends JsonChart {

	private static final String DATA_FORMAT = "{label : '%s', data : %s}";

	public abstract String getColors();

	public abstract String getData();

	@Override
	public String getDataFormat() {
		return DATA_FORMAT;
	}

	protected String data(String label, String value) {
		return String.format(getDataFormat(), label, value);
	}
}