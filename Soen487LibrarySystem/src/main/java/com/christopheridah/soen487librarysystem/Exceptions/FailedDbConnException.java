/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.soen487librarysystem.Exceptions;

/**
 *
 * @author youssefsb
 */
public class FailedDbConnException extends Exception {

    /**
     * Creates a new instance of <code>FiledDbConnException</code> without
     * detail message.
     */
    public FailedDbConnException() {
    }

    /**
     * Constructs an instance of <code>FiledDbConnException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FailedDbConnException(String msg) {
        super(msg);
    }
}
