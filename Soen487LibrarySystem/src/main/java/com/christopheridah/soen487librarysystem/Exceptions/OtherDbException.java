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
public class OtherDbException extends Exception {

    /**
     * Creates a new instance of <code>DataBaseException</code> without detail
     * message.
     */
    public OtherDbException() {
    }

    /**
     * Constructs an instance of <code>DataBaseException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OtherDbException(String msg) {
        super(msg);
    }
}
