/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loansystem;

/**
 *
 * @author chris
 */
public class LoanException extends Exception {
    
    public LoanException () {
        
    }
    
    public LoanException (String message) {
        
        super (message);
    }
    
}
