/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.model.to;

import java.util.Objects;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class KeyValue {

	private String key;
	private String value;

	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KeyValue) {
			KeyValue kv = (KeyValue) obj;
			return (kv.key.equals(this.key));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.key);
	}
}
