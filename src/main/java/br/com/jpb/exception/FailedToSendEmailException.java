/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.exception;

/**
 *
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class FailedToSendEmailException extends Exception {

    public FailedToSendEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
