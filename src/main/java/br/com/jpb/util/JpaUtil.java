package br.com.jpb.util;

import java.util.List;
import javax.persistence.TypedQuery;

public class JpaUtil {

	private JpaUtil() {
	}

	public static <T> T uniqueResultOrNull(TypedQuery<T> query) {
		return uniqueResultOrElse(query, null);
	}

	public static <T> T uniqueResultOrElse(final TypedQuery<T> query,
			final T defaultValue) {
		final List<T> resultList = query.setMaxResults(1).getResultList();
		return resultList.isEmpty() ? defaultValue : resultList.get(0);
	}

	public static <T> T notNullUniqueResultOrElse(final TypedQuery<T> query,
			final T defaultValue) {
		final List<T> resultList = query.setMaxResults(1).getResultList();
		return (resultList.isEmpty() || null == resultList.get(0)) ? defaultValue
				: resultList.get(0);
	}
}