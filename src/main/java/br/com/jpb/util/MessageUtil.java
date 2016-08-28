/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class MessageUtil {

	private static final ResourceBundle baseBundle = ResourceBundle.getBundle("br/com/jpb/bundles/messages");
	private static final List<ResourceBundle> bundles = new ArrayList<>();

	public static void addBundle(String baseName) {
		bundles.add(ResourceBundle.getBundle(baseName));
	}

	public static String getString(String key) {
		return getFromBundles(key);
	}

	public static String getString(String key, Object... args) {
		final String fromBundle = getFromBundles(key);

		if (fromBundle == null) {
			return null;
		}

		return String.format(fromBundle, args);
	}

	private static String getFromBundles(final String key) {
		for (ResourceBundle bundle : bundles) {
			try {
				return bundle.getString(key);
			} catch (Exception e) {
				// will try next bundle
				continue;
			}
		}

		try {
			return baseBundle.getString(key);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getUnexpectedErrorMessage() {
		return getString("error.unexpected");
	}
}