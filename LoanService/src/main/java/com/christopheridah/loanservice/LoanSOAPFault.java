/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loanservice;

import javax.xml.ws.WebFault;

/**
 *
 * @author chris
 */

@WebFault (name = "LoanSOAPFault", targetNamespace = "http://loanservice.christopheridah.com/" )
public class LoanSOAPFault extends Exception {

    public LoanSOAPFault() {
    }

    public LoanSOAPFault(String message) {
        super(message);
    }

    public LoanSOAPFault(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
