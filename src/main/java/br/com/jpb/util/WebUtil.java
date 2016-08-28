/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class WebUtil {

	private static final String LINK_TEMPLATE = "<a href=\"#\" class=\"abbreviatedText\">...</a>";

	public static String abbreviateWithLinkToDialog(String originalText, int size) {
		if (StringUtils.isEmpty(originalText)) {
			return StringUtils.EMPTY;
		}
		if (originalText.length() <= size) {
			return originalText;
		} else {
			return StringUtil.replaceLast(StringUtils.abbreviate(originalText, size), "...", LINK_TEMPLATE);
		}
	}
}
