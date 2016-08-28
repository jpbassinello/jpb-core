package br.com.jpb.charts.morris;

import br.com.jpb.charts.TwoAxisValue;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

public abstract class MorrisMultiSeriesChart extends MorrisChart {

	private static final Joiner JOINER = Joiner.on(", ");

	private static final String DATA_FORMAT = "%s : '%s'";

	public static MorrisMultiSeriesChart twoAxisChart(final List<TwoAxisValue> twoAxisValues,
			final List<String> colors) {
		return new MorrisMultiSeriesChart() {

			final String[] keys = {"x", "y"};

			@Override
			public String getData() {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				final List<String> dataByMonitor = new ArrayList<>();
				for (TwoAxisValue twoAxisValue : twoAxisValues) {
					String[] values = {twoAxisValue.getX(), twoAxisValue.getY()};
					dataByMonitor.add(data(keys, values));
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
			public String getYKeys() {
				return "[" + singleQuote("y") + "]";
			}

			@Override
			public String getXKey() {
				return singleQuote("x");
			}

			@Override
			public String getLabels() {
				return "[" + singleQuote("Total") + "]";
			}
		};
	}

	public abstract String getXKey();

	public abstract String getYKeys();

	public abstract String getLabels();

	protected String data(String[] keys, Object[] values) {
		final List<String> datas = new ArrayList<>();
		for (int i = 0; i < keys.length; i++) {
			datas.add(String.format(DATA_FORMAT, keys[i], values[i]));
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(JOINER.join(datas));
		sb.append("}");
		return sb.toString();
	}
}