package br.com.jpb.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class Importer<T> extends BaseExporterImporter<T> {

	protected int rowsInHeader = 1;

	protected Importer(Class<T> clazz) {
		super(clazz);
	}

	public List<T> importFile(File file) {
		try {
			return importFile(new BufferedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Error while reading the file", e);
		}
	}

	protected abstract void init(InputStream is);

	protected abstract List<List<String>> rows(int rowsInHeader);

	public List<T> importFile(InputStream inputStream) {

		InputStream bis = new BufferedInputStream(inputStream);

		init(bis);

		final List<Field> fields = getOrderedFieldsAnnotatedWithColumn();

		final List<T> list = new ArrayList<>();

		final List<List<String>> rows = rows(rowsInHeader);
		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			T t = newInstance();
			List<String> columnValues = rows.get(rowIndex);
			for (int i = 0; i < fields.size(); i++) {
				if (i >= columnValues.size()) {
					break;
				}
				Field column = fields.get(i);
				Class<?> type = column.getType();
				String obj = columnValues.get(i);
				if (obj == null) {
					continue;
				}
				obj = obj.trim();

				ExporterDateTime dateTime = column.getAnnotation(ExporterDateTime.class);
				if (dateTime != null) {
					try {
						dateTimeAsLdtLong(t, column, type, obj);
					} catch (Exception e) {
						try {
							dateTimeAsString(t, column, type, obj, dateTime);
						} catch (Exception e1) {
						}
					}
					continue;
				}

				ExporterNumeric numeric = column.getAnnotation(ExporterNumeric.class);
				if (numeric != null) {
					double value = Double.parseDouble((String) obj);
					if (Integer.class.equals(type) || int.class.equals(type)) {
						columnSet(t, column, Double
								.valueOf(value)
								.intValue());
					}
					if (Long.class.equals(type) || long.class.equals(type)) {
						columnSet(t, column, Double
								.valueOf(value)
								.longValue());
					}
					if (BigDecimal.class.equals(type)) {
						columnSet(t, column,
								BigDecimal
										.valueOf(value)
										.setScale(numeric.scale(), numeric.roundingMode()));
					}
					continue;
				}

				if (Boolean.class.equals(column.getType()) || boolean.class.equals(column.getType())) {
					boolean value = Boolean.valueOf(obj);
					columnSet(t, column, value);
					continue;
				}

				if (String.class.equals(column.getType())) {
					columnSet(t, column, (String) obj);
					continue;
				}
			}
			list.add(t);
		}

		return list;
	}

	private void dateTimeAsLdtLong(T t, Field column, Class<?> type, String obj) {
		long value = Long.valueOf(obj);
		if (LocalDateTime.class.equals(type)) {
			LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
			columnSet(t, column, ldt);
		}
		if (LocalDate.class.equals(type)) {
			LocalDate ld = LocalDateTime
					.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
					.toLocalDate();
			columnSet(t, column, ld);
		}
	}

	private void dateTimeAsString(T t, Field column, Class<?> type, String obj, ExporterDateTime dateTime) {
		String date = (String) obj;
		if (LocalDateTime.class.equals(type)) {
			columnSet(t, column, LocalDateTime.parse(date, DateTimeFormatter.ofPattern(dateTime.formatDateTime())));
		}
		if (LocalDate.class.equals(type)) {
			columnSet(t, column, LocalDate.parse(date, DateTimeFormatter.ofPattern(dateTime.formatDate())));
		}
		if (YearMonth.class.equals(type)) {
			columnSet(t, column, YearMonth.parse(date, DateTimeFormatter.ofPattern(dateTime.formatMonth())));
		}
	}

	private void columnSet(T t, Field column, Object obj) {
		try {
			column.setAccessible(true);
			column.set(t, obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException("Reflection error.", e);
		}
	}

}
