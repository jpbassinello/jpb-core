package br.com.jpb.data;

import br.com.jpb.util.StringUtil;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class BaseExporterImporter<T> {
	protected final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

	private final Class<T> clazz;

	private final Set<Class<?>> RETURN_TYPES_FOR_NUMERIC = new HashSet<Class<?>>(
			Arrays.asList(Integer.class, int.class, Long.class, long.class, BigDecimal.class));
	private final Set<Class<?>> RETURN_TYPES_FOR_DATETIME = new HashSet<Class<?>>(
			Arrays.asList(LocalDate.class, LocalDateTime.class, YearMonth.class));

	protected Set<String> ignoredFields = new HashSet<>();

	protected BaseExporterImporter(Class<T> clazz) {
		this.clazz = clazz;
	}

	private Constructor<T> constructor() {
		try {
			return clazz.getDeclaredConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException("Reflection error.", e);
		}
	}

	protected T newInstance() {
		try {
			return constructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
				InvocationTargetException e) {
			throw new IllegalStateException("Reflection error.", e);
		}
	}

	protected List<String> getHeaders() {
		return getOrderedFieldsAnnotatedWithColumn()
				.stream()
				.map(field -> field
						.getAnnotation(ExporterColumn.class)
						.headerText())
				.collect(Collectors.toList());
	}

	protected Map<Integer, Field> getColumnsFieldsAnnotatedWithColumnGroupByIndex() {
		final Map<Integer, Field> columnsTypesByIndex = new HashMap<>();

		for (Field field : getOrderedFieldsAnnotatedWithColumn()) {
			ExporterColumn exporterColumn = field.getAnnotation(ExporterColumn.class);
			columnsTypesByIndex.put(exporterColumn.index(), field);
		}

		return columnsTypesByIndex;
	}

	protected List<Field> getOrderedFieldsAnnotatedWithColumn() {
		final Set<Field> fields = fieldsAnnotatedWithColumn();

		List<Field> list = new ArrayList<>(fields);

		list.sort(Comparator.comparingInt(f -> f
				.getAnnotation(ExporterColumn.class)
				.index()));

		return list;
	}

	protected void validateConfiguration() {
		fieldsAnnotatedWithColumn().forEach(new Consumer<Field>() {
			@Override
			public void accept(Field field) {
				Class<?> returnType = field.getType();

				ExporterDateTime dateTime = field.getAnnotation(ExporterDateTime.class);
				if (dateTime != null && !RETURN_TYPES_FOR_DATETIME.contains(returnType)) {
					configurationError(field, RETURN_TYPES_FOR_DATETIME);
				}

				ExporterNumeric numeric = field.getAnnotation(ExporterNumeric.class);
				if (numeric != null && !RETURN_TYPES_FOR_NUMERIC.contains(returnType)) {
					configurationError(field, RETURN_TYPES_FOR_NUMERIC);
				}
			}
		});
	}

	private void configurationError(Field field, Set<Class<?>> availableTypes) {
		throw new IllegalStateException("The configuration for field " + field
				.getName() + " is invalid. Availables types are: " + StringUtil.DEFAULT_JOINER.join(availableTypes));
	}

	private Set<Field> fieldsAnnotatedWithColumn() {
		@SuppressWarnings("unchecked")
		final Set<Field> fields = ReflectionUtils
				.getAllFields(clazz, ReflectionUtils.<Field>withAnnotation(ExporterColumn.class));

		return fields
				.stream()
				.filter(field -> !ignoredFields.contains(field.getName()))
				.collect(Collectors.toSet());
	}

	protected enum ColumnType {
		BOOLEAN,
		DATE_TIME,
		NUMERIC,
		STRING;
	}

}
