/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.enums;

import br.com.jpb.component.Messages;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public enum TrueOrFalse implements ValueLabelEnum {

	TRUE("true"),
	FALSE("false");

	private final String label;

	private TrueOrFalse(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getValue() {
		return label;
	}

	@Override
	public String getFormattedValue() {
		return Messages.getMessage(label);
	}
}