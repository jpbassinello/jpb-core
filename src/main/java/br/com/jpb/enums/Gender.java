/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.enums;

import br.com.jpb.component.Messages;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@RequiredArgsConstructor
public enum Gender {

	MALE("male"),
	FEMALE("female"),
	UNKNOWN("unknown");

	private final String value;

	public static Gender getByValue(final String value) {
		return Stream
				.of(values())
				.filter(g -> g.value.equals(value))
				.findFirst()
				.orElse(null);
	}

	public String getPtBr() {
		return Messages.getMessage(value);
	}
}
