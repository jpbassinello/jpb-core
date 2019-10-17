/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.enums;

import br.com.jpb.component.Messages;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public enum YesOrNo implements ValueLabelEnum {

	YES("yes"),
	NO("no");

	private final String label;

	private YesOrNo(String label) {
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
