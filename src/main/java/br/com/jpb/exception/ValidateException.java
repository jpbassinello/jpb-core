/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.exception;

/**
 *
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class ValidateException extends BaseException {

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable t) {
        super(message, t);
    }

    public ValidateException(String message, Object... args) {
        super(message, args);
    }
}