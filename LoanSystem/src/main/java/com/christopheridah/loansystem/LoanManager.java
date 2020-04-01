/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loansystem;

import com.christopheridah.loancore.Loan;
import com.christopheridah.loansystem.dataAccessLayer.LoanTableGateway;
import java.time.LocalDate;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author chris
 */
public class LoanManager {

    private static LoanManager instance = null;
    private static final Object lock = new Object();
    private LoanTableGateway gateway;
    private ObjectMapper objectMapper = new ObjectMapper();

    public LoanManager() {

        try {
            gateway = new LoanTableGateway();
        } catch (Exception e) {
            e.toString();
        }
    }

    public static LoanManager getInstance() {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LoanManager();
                }
            }
        }
        return instance;
    }

    public Loan getLoan(int id) {

        try {
            return gateway.getLoan(id);
        } catch (Exception e) {
            e.toString();
            return new Loan();
        }
    }

    public String getAllLoans() {

        try {
            return objectMapper.writeValueAsString(gateway.getAllLoans());
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String getBookLoans(String bookID) {
        try {
            return objectMapper.writeValueAsString(gateway.getBookLoans(bookID));
        } catch (Exception e) {
            return e.toString();
        }

    }

    public String getMemberLoans(int memberID) {

        try {
            return objectMapper.writeValueAsString(gateway.getMemberLoans(memberID));

        } catch (Exception e) {
            return e.toString();
        }
    }

    public int borrowBook(int memberID, String bookID) {

        try {
            return gateway.borrowBook(memberID, bookID);
        } catch (Exception e) {
            
            String reason = e.toString();
            return -3;
        }

    }

    public void editLoan(int loanID, int memberID, String bookID, LocalDate borrowDate, LocalDate returnDate, boolean returned) {

        try {
            gateway.editLoan(loanID, memberID, bookID, borrowDate, returnDate, returned);
        } catch (Exception e) {

        }

    }

    public void returnBook(String bookID) {

        try {
            gateway.returnBook(bookID);
        } catch (Exception e) {
                
            String reason = e.toString();
            
            String nothing = "";
        }
    }

    public int deleteLoan(int loanID) {

        try {
            return gateway.deleteLoan(loanID);
        } catch (Exception e) {
            return -3;
        }
    }

    public boolean loanExists(int id) {

        try {
            return gateway.loanExists(id);
        } catch (Exception e) {
            return false;
        }

    }
}
