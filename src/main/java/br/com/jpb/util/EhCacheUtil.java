package br.com.jpb.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.Map;
import java.util.Set;

public class EhCacheUtil {

	@SuppressWarnings("unchecked")
	public static <T> Set<T> getSetFromCache(Cache cache, String key) {
		Element element = cache.get(key);
		return element == null ? null : (Set<T>) element.getObjectValue();
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getMapFromCache(Cache cache, String key) {
		Element element = cache.get(key);
		return element == null ? null : (Map<K, V>) element.getObjectValue();
	}
}
