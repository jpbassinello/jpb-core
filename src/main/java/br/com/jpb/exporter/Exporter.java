package br.com.jpb.exporter;

import br.com.jpb.util.DateTimeUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class Exporter<T> extends BaseExporterImporter<T> {

	protected boolean noHeader = false;

	protected Exporter(Class<T> clazz) {
		super(clazz);
	}

	protected abstract void init(String fileName);

	protected abstract void initHeader();

	protected abstract void writeHeaderColumn(String value, int colIndex);

	protected abstract void writeEmptyColumn(int rowIndex, int colIndex);

	protected abstract void writeColumn(Date value, String dateFormat, int rowIndex, int colIndex);

	protected abstract void writeColumn(double value, int rowIndex, int colIndex);

	protected abstract void writeColumn(int value, int rowIndex, int colIndex);

	protected abstract void writeColumn(boolean value, int rowIndex, int colIndex);

	protected abstract void writeColumn(String value, int rowIndex, int colIndex);

	protected abstract void writeTheFile(OutputStream os);

	protected abstract void close();

	public File exportFile(List<T> data, String fileName) {

		validateConfiguration();

		init(fileName);

		if (!noHeader) {
			initHeader();
			List<String> headers = getHeaders();
			for (int i = 0; i < headers.size(); i++) {
				writeHeaderColumn(headers.get(i), i);
			}
		}

		for (int i = 0; i < data.size(); i++) {
			int rowIndex = i + 1;
			Map<Integer, Field> fieldsByIndex = getColumnsFieldsAnnotatedWithColumnGroupByIndex();
			int maxIndex = fieldsByIndex
					.keySet()
					.stream()
					.max(Comparator.naturalOrder())
					.orElse(0);
			int colIndex = -1;
			for (int j = 0; j <= maxIndex; j++) {
				Field column = fieldsByIndex.get(j);
				if (column == null) {
					continue;
				}
				colIndex++;
				T t = data.get(i);
				Object obj = reflectionGet(t, column);

				if (obj == null) {
					writeEmptyColumn(rowIndex, colIndex);
					continue;
				}

				ExporterDateTime dateTime = column.getAnnotation(ExporterDateTime.class);
				if (dateTime != null) {
					writeColumn(dateCellValue(obj, dateTime), rowIndex, colIndex);
					continue;
				}

				ExporterNumeric numeric = column.getAnnotation(ExporterNumeric.class);
				if (numeric != null) {
					writeColumn(numericCellValue(obj, numeric), rowIndex, colIndex);
					continue;
				}

				if (Boolean.class.equals(column.getType()) || boolean.class.equals(column.getType())) {
					writeColumn((Boolean) obj, rowIndex, colIndex);
					continue;
				}

				if (String.class.equals(column.getType())) {
					writeColumn((String) obj, rowIndex, colIndex);
					continue;
				}

				throw new IllegalStateException(
						"Invalid configuration. Return type " + column.getType() + " is not allowed.");
			}

		}

		return createFile(fileName);
	}

	protected File createFile(String fileName) {
		File file;
		try {
			file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + fileName);
			BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
			writeTheFile(fos);
			fos.close();
			close();
		} catch (IOException e) {
			throw new IllegalStateException("Erro while closing the streams", e);
		}
		return file;
	}

	private double numericCellValue(Object obj, ExporterNumeric numeric) {
		double value = 0;
		if (obj instanceof Integer) {
			value = defaultScaleAndRounding(BigDecimal.valueOf((Integer) obj), numeric);
		}
		if (obj instanceof Long) {
			value = defaultScaleAndRounding(BigDecimal.valueOf((Long) obj), numeric);
		}
		if (obj instanceof BigDecimal) {
			value = defaultScaleAndRounding((BigDecimal) obj, numeric);
		}
		return value;
	}

	private double defaultScaleAndRounding(BigDecimal bigDecimal, ExporterNumeric numeric) {
		return bigDecimal
				.setScale(numeric.scale(), numeric.roundingMode())
				.doubleValue();
	}

	private String dateCellValue(Object obj, ExporterDateTime format) {
		LocalDateTime date = null;
		String pattern = null;
		if (obj instanceof LocalDateTime) {
			date = ((LocalDateTime) obj);
			pattern = format.formatDateTime();
		}
		if (obj instanceof LocalDate) {
			date = ((LocalDate) obj).atTime(LocalTime.MIDNIGHT);
			pattern = format.formatDate();
		}
		if (obj instanceof YearMonth) {
			date = ((YearMonth) obj)
					.atDay(1)
					.atTime(LocalTime.MIDNIGHT);
			pattern = format.formatMonth();
		}
		return DateTimeUtil.formatInPattern(date, pattern);
	}

	private Object reflectionGet(T t, Field field) {
		try {
			field.setAccessible(true);
			return field.get(t);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new IllegalStateException("Reflection Error", e);
		}
	}

	public File exportDataMatrixToFile(List<String> headers, List<List<Object>> matrix,
			String fileName) {
		init(fileName);

		initHeader();
		for (int i = 0; i < headers.size(); i++) {
			writeHeaderColumn(headers.get(i), i);
		}

		for (int i = 0; i < matrix.size(); i++) {
			List<?> row = matrix.get(i);
			for (int j = 0; j < row.size(); j++) {
				Object obj = row.get(j);
				obj = obj == null ? "" : obj;

				int rowIndex = i + 1;

				if (obj instanceof Date) {
					writeColumn((Date) obj, DEFAULT_DATE_TIME_FORMAT, rowIndex, j);
				} else {
					if (obj instanceof Number) {
						if (obj instanceof Integer) {
							writeColumn((Integer) obj, rowIndex, j);
						} else {
							writeColumn(((Number) obj).doubleValue(), rowIndex, j);
						}
					} else {
						if (obj instanceof Boolean) {
							writeColumn((Boolean) obj, rowIndex, j);
						} else {
							writeColumn(obj.toString(), rowIndex, j);
						}
					}
				}
			}
		}

		return createFile(fileName);
	}
}