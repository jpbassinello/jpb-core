/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.exception;

import java.io.IOException;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class BusinessException extends BaseException {

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Object... args) {
		super(message, args);
	}

	public BusinessException(String message, IOException ex) {
		super(message, ex);
	}
}
