package br.com.jpb.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public final class CollectionUtil {

	public static <T> Set<T> differenceBetween(Collection<T> first,
			Collection<T> second) {
		return first.stream()
				.filter(((Predicate<T>) second::contains).negate())
				.collect(Collectors.toSet());
	}

	public static <T> void syncronizeInFirst(Collection<T> first,
			Collection<T> second) {
		Set<T> remove = differenceBetween(first, second);
		Set<T> newT = differenceBetween(second, first);

		first.removeAll(remove);
		first.addAll(newT);
	}

	public static boolean isNullOrEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static <K, V extends Comparable<V>> Map<K, V> sortMapByValues(
			final Map<K, V> map) {
		Comparator<K> valueComparator = new Comparator<K>() {
			public int compare(K k1, K k2) {
				int compare = map.get(k2).compareTo(map.get(k1));
				if (compare == 0)
					return 1;
				else
					return compare;
			}
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}

}