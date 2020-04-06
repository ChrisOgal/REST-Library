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
public class InvalidPublisherException extends Exception {

    /**
     * Creates a new instance of <code>InvalidPublisherException</code> without
     * detail message.
     */
    public InvalidPublisherException() {
    }

    /**
     * Constructs an instance of <code>InvalidPublisherException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidPublisherException(String msg) {
        super(msg);
    }
}
