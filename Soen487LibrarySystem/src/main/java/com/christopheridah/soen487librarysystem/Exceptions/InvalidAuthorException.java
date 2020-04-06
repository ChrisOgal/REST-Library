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
public class InvalidAuthorException extends Exception {

    /**
     * Creates a new instance of <code>AuthorNotFoundException</code> without
     * detail message.
     */
    public InvalidAuthorException() {
    }

    /**
     * Constructs an instance of <code>AuthorNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidAuthorException(String msg) {
        super(msg);
    }
}
