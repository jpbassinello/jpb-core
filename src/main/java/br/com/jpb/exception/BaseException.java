/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.exception;

import br.com.jpb.component.Messages;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public abstract class BaseException extends Exception {

	protected Object[] args;

	public BaseException() {
		super("erro.inesperado");
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Object... args) {
		super(message);
		this.args = args;
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message, Throwable cause, Object... args) {
		super(message, cause);
		this.args = args;
	}

	public String getDetailedMessage() {
		return Messages.getMessage(getMessage(), args);
	}
}