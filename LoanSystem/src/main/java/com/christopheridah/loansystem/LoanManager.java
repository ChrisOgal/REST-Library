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

    public LoanManager() throws LoanException {

        try {
            gateway = new LoanTableGateway();
        } catch (Exception e) {

           throw new LoanException(e.getMessage(), e);

        }
    }

    public static LoanManager getInstance() throws LoanException {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LoanManager();
                }
            }
        }
        return instance;
    }

    public Loan getLoan(int id) throws LoanException {

        try {
            return gateway.getLoan(id);
        } catch (Exception e) {

            throw new LoanException(e.getMessage(), e);

        }
    }

    public String getAllLoans() throws LoanException {

        try {
            return objectMapper.writeValueAsString(gateway.getAllLoans());
        } catch (Exception e) {

            throw new LoanException(e.getMessage(), e);

        }
    }

    public String getBookLoans(String bookID) throws LoanException {
        try {
            return objectMapper.writeValueAsString(gateway.getBookLoans(bookID));
        } catch (Exception e) {

            throw new LoanException(e.getMessage(), e);

        }

    }

    public String getMemberLoans(int memberID) throws LoanException {

        try {
            return objectMapper.writeValueAsString(gateway.getMemberLoans(memberID));

        } catch (Exception e) {

            throw new LoanException(e.getMessage(), e);

        }
    }

    public int borrowBook(int memberID, String bookID) throws LoanException {

        try {
            return gateway.borrowBook(memberID, bookID);
        } catch (Exception e) {
            

            throw new LoanException(e.getMessage(), e);

        }

    }

    public void editLoan(int loanID, int memberID, String bookID, LocalDate borrowDate, LocalDate returnDate, boolean returned) throws LoanException {

        try {
            gateway.editLoan(loanID, memberID, bookID, borrowDate, returnDate, returned);
        } catch (Exception e) {

throw new LoanException(e.getMessage(), e);

        }

    }

    public void returnBook(String bookID) throws LoanException {

        try {
            gateway.returnBook(bookID);
        } catch (Exception e) {
                

            throw new LoanException(e.getMessage(), e);

        }
    }

    public int deleteLoan(int loanID) throws LoanException {

        try {
            return gateway.deleteLoan(loanID);
        } catch (Exception e) {

            throw new LoanException(e.getMessage(), e);

        }
    }

    public boolean loanExists(int id) throws LoanException {

        try {
            return gateway.loanExists(id);
        } catch (Exception e) {

            throw new LoanException(e.getMessage(), e);
        }

    }
}
