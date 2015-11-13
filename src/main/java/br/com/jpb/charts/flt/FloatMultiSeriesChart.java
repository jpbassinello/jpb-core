package br.com.jpb.charts.flt;

import java.util.ArrayList;
import java.util.List;

import br.com.jpb.charts.TwoAxisValue;

import com.google.common.base.Joiner;

public abstract class FloatMultiSeriesChart extends FloatChart {

	private static final Joiner JOINER = Joiner.on(", ");

	private static final String DATA_FORMAT = "[%s, %s]";

	public abstract String getLabelAxisX();

	public abstract String getLabelAxisY();

	protected String data(long... values) {
		final List<String> datas = new ArrayList<>();
		for (int i = 0; i < values.length; i++) {
			datas.add(String.format(DATA_FORMAT, i, values[i]));
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(JOINER.join(datas));
		sb.append("]");
		return sb.toString();
	}

	public static FloatMultiSeriesChart twoAxisChart(
			final List<TwoAxisValue> twoAxisValues,
			final List<String> colors, String labelAxisX, String labelAxisY) {
		return new FloatMultiSeriesChart() {
			@Override
			public String getData() {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				final List<String> dataByMonitor = new ArrayList<>();
				for (TwoAxisValue twoAxisValue : twoAxisValues) {
					String label = twoAxisValue.getX();
					long value = Long.valueOf(twoAxisValue.getY());
					dataByMonitor.add(data(label, data(value)));
				}
				sb.append(JOINER.join(dataByMonitor));
				sb.append("]");
				return sb.toString();
			}

			@Override
			public String getColors() {
				if (colors == null) {
					return null;
				}
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				final List<String> colorsData = new ArrayList<>();
				for (String color : colors) {
					colorsData.add(singleQuote(color));
				}
				sb.append(JOINER.join(colorsData));
				sb.append("]");
				return sb.toString();
			}

			@Override
			public String getLabelAxisX() {
				return singleQuote(labelAxisX);
			}

			@Override
			public String getLabelAxisY() {
				return singleQuote(labelAxisY);
			}
		};
	}
}