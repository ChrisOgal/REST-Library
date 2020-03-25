/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loansystem;

import com.christopheridah.loancore.Loan;
import com.christopheridah.loansystem.dataAccessLayer.LoanTableGateway;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author chris
 */
public class LoanManager {

    private static LoanManager instance = null;
    private static final Object lock = new Object();
    private LoanTableGateway gateway;

    public LoanManager() throws SQLException {
        gateway = new LoanTableGateway();
    }

    public static LoanManager getInstance() throws SQLException {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LoanManager();
                }
            }
        }

        return instance;
    }

    public Loan getLoan(int id) throws SQLException {
        return gateway.getLoan(id);
    }

    public List<Loan> getAllLoans() {
        return gateway.getAllLoans();
    }

    public int borrowBook(int memberID, String bookID) throws LoanException, SQLException {
        return gateway.borrowBook(memberID, bookID);
    }

    public void editLoan(int loanID, int memberID, String bookID, LocalDate borrowDate, LocalDate returnDate, boolean returned) throws SQLException {

        gateway.editLoan(loanID, memberID, bookID, borrowDate, returnDate, returned);
    }

    public void returnBook(String bookID) throws SQLException, LoanException {
        gateway.returnBook(bookID);
    }

    public int deleteLoan(int loanID) throws SQLException {
       
        return gateway.deleteLoan(loanID);
    }


    public boolean loanExists(int id) throws SQLException {
        return gateway.loanExists(id);
    }
}
