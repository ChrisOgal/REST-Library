/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loansystem.dataAccessLayer;

import com.christopheridah.loancore.Loan;
import com.christopheridah.loansystem.LoanException;
import com.christopheridah.loansystem.MemberManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chris
 */
public class LoanTableGateway {

    private static final String databaseURL = "jdbc:derby://localhost:1527/Library";
    private static final Object lock = new Object();
    private static Connection conn;

    public LoanTableGateway() throws SQLException {
        conn = DriverManager.getConnection(databaseURL);
    }

    public Loan getLoan(int id) throws SQLException {
        Loan requestedLoan = new Loan();

        if (loanExists(id)) {
            String loanQuery = "select * from Loan where id = ?";

            try (PreparedStatement ps = conn.prepareStatement(loanQuery)) {
                ps.setInt(1, id);

                try (ResultSet rset = ps.executeQuery()) {
                    rset.next();

                    requestedLoan.setId(rset.getInt(1));
                    requestedLoan.setBorrower(rset.getInt(2));
                    requestedLoan.setBookID(rset.getString(3));
                    requestedLoan.setBorrowDate(rset.getDate(4).toString());
                    requestedLoan.setReturnDate(rset.getDate(5).toString());
                    requestedLoan.setReturned(rset.getBoolean(6));
                }
            }
        }
        return requestedLoan;
    }

    public Loan getLoan(String id) throws SQLException {

        Loan requestedLoan = new Loan();

        if (loanExists(id)) {
            String loanQuery = "select * from Loan where bookID = ?";

            try (PreparedStatement ps = conn.prepareStatement(loanQuery)) {
                ps.setString(1, id);

                try (ResultSet rset = ps.executeQuery()) {
                    rset.next();

                    requestedLoan.setId(rset.getInt(1));
                    requestedLoan.setBorrower(rset.getInt(2));
                    requestedLoan.setBookID(rset.getString(3));
                    requestedLoan.setBorrowDate(rset.getDate(4).toString());
                    requestedLoan.setReturnDate(rset.getDate(5).toString());
                    requestedLoan.setReturned(rset.getBoolean(6));
                }
            }
        }
        return requestedLoan;
    }

    public Loan getLoan(String id, boolean returned) throws SQLException {

        Loan requestedLoan = new Loan();

        if (loanExists(id)) {
            String loanQuery = "select * from Loan where bookID = ? and returned = ?";

            try (PreparedStatement ps = conn.prepareStatement(loanQuery)) {
                ps.setString(1, id);
                ps.setBoolean(2, returned);

                try (ResultSet rset = ps.executeQuery()) {
                    rset.next();

                    requestedLoan.setId(rset.getInt(1));
                    requestedLoan.setBorrower(rset.getInt(2));
                    requestedLoan.setBookID(rset.getString(3));
                    requestedLoan.setBorrowDate(rset.getDate(4).toString());
                    requestedLoan.setReturnDate(rset.getDate(5).toString());
                    requestedLoan.setReturned(rset.getBoolean(6));
                }
            }
        }
        return requestedLoan;
    }

    public List<Loan> getAllLoans() throws SQLException {

        String query = "select * from Loan";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            try (ResultSet rset = ps.executeQuery()) {
                return populateList(rset);
            }
        }

    }

    public List<Loan> getMemberLoans(int memberID) throws SQLException {

        String query = "select * from Loan where memberID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, memberID);

            try (ResultSet rset = ps.executeQuery()) {

                return populateList(rset);

            }
        }

    }

    public List<Loan> getBookLoans(String bookID) throws SQLException {

        String query = "select * from Loan where bookID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, bookID);

            try (ResultSet rset = ps.executeQuery()) {
                return populateList(rset);
            }

        }

    }

    public int borrowBook(int memberID, String bookID) throws LoanException, SQLException {
        int confirmedLoan = 0;

        if (!checkAvailability(bookID)) {
            throw new LoanException("Book is not available");
        }
        if (!MemberManager.getInstance().memberExists(memberID)) {
            throw new LoanException("Member does not exist. Please register first.");
        } else {
            int loanID = generateID();

            if (checkDuplicateId(loanID) > 0) {
                throw new LoanException("Attempting to duplicate loan ID.");
            } else {
                LocalDate borrowDate = LocalDate.now();
                LocalDate returnDate = generateReturnDate(borrowDate);

                String insertLoan = "INSERT INTO Loan VALUES (?,?,?,?,?,?)";

                try (PreparedStatement ps = conn.prepareStatement(insertLoan)) {
                    ps.setInt(1, loanID);
                    ps.setInt(2, memberID);
                    ps.setString(3, bookID);
                    ps.setDate(4, Date.valueOf(borrowDate));
                    ps.setDate(5, Date.valueOf(returnDate));
                    ps.setBoolean(6, false);

                    synchronized (lock) {
                        ps.executeUpdate();

                        String confirmID = "select MAX(id) from Loan";

                        try (PreparedStatement ps2 = conn.prepareStatement(confirmID)) {
                            try (ResultSet rset = ps2.executeQuery()) {

                                rset.next();
                                confirmedLoan = rset.getInt(1);
                            }
                        }
                    }
                }
            }
        }

        return confirmedLoan;
    }

    public void editLoan(int loanID, int memberID, String bookID, LocalDate borrowDate, LocalDate returnDate, boolean returned) throws SQLException {

        if (loanExists(loanID)) {
            Loan dirtyLoan = getLoan(loanID);
            Loan cleanLoan = new Loan(loanID, memberID, bookID, borrowDate.toString(), returnDate.toString());
            cleanLoan.setReturned(returned);

            if (!dirtyLoan.equals(cleanLoan)) {
                String updateLoan = "update Loan set memberID = ?, bookID = ?, borrowDate = ?, returnDate = ?, returned = ? where id = ?";

                try (PreparedStatement ps = conn.prepareStatement(updateLoan)) {
                    ps.setInt(1, memberID);
                    ps.setString(2, bookID);
                    ps.setDate(3, Date.valueOf(borrowDate));
                    ps.setDate(4, Date.valueOf(returnDate));
                    ps.setBoolean(5, returned);
                    ps.setInt(6, loanID);

                    ps.executeUpdate();
                }
            }

        }
    }

    public void returnBook(String bookID) throws SQLException, LoanException {

        if (!checkAvailability(bookID)) {
            Loan requestedLoan = getLoan(bookID, false);
            editLoan(requestedLoan.getId(), requestedLoan.getBorrower(), bookID, LocalDate.parse(requestedLoan.getBorrowDate()), LocalDate.parse(requestedLoan.getReturnDate()), true);
        }
    }

    public int deleteLoan(int loanID) throws SQLException {

        if (loanExists(loanID)) {
            String deleteQuery = "delete from Loan where id = ?";

            try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {

                ps.setInt(1, loanID);

                return ps.executeUpdate();
            }
        } else {
            return loanID;
        }
    }

    public boolean loanExists(int id) throws SQLException {

        String query = "select * from Loan where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rset = ps.executeQuery()) {
                rset.next();

                if (rset.getInt(1) >= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean loanExists(String id) throws SQLException {

        String query = "select * from Loan where bookID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);

            try (ResultSet rset = ps.executeQuery()) {
                rset.next();

                if (rset.getInt(1) >= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private LocalDate generateReturnDate(LocalDate date) {

        LocalDate returnDate = date.plusWeeks(2);

        DayOfWeek potentialReturnDay = DayOfWeek.of(returnDate.get(ChronoField.DAY_OF_WEEK));

        switch (potentialReturnDay) {
            case SATURDAY:
                returnDate = returnDate.plusDays(2);
                break;
            case SUNDAY:
                returnDate = returnDate.plusDays(1);
                break;
        }

        return returnDate;
    }

    private int generateID() throws SQLException {
        int additionID = 0;

        synchronized (lock) {
            String lastID = "Select MAX(id) from Loan";

            try (PreparedStatement ps = conn.prepareStatement(lastID)) {

                try (ResultSet rset = ps.executeQuery()) {

                    rset.next();

                    additionID = rset.getInt(1) + 1;
                }
            }
        }

        return additionID;
    }

    private int checkDuplicateId(int id) throws SQLException {

        int duplicate = 0;

        String duplicateTest = "select COUNT(id) from Loan where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(duplicateTest)) {

            ps.setInt(1, id);

            try (ResultSet rset = ps.executeQuery()) {
                rset.next();

                duplicate = rset.getInt(1);
            }
        }

        return duplicate;
    }

    private boolean checkAvailability(String bookID) throws SQLException {

        String availableTest = "select COUNT(*) from Loan where bookID = ? and returned = ?";

        try (PreparedStatement ps = conn.prepareStatement(availableTest)) {
            ps.setString(1, bookID);
            ps.setBoolean(2, false);

            try (ResultSet rset = ps.executeQuery()) {
                rset.next();

                if (rset.getInt(1) >= 1) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<Loan> populateList(ResultSet rset) throws SQLException {
        List<Loan> requestedList = new ArrayList<>();

        while (rset.next()) {
            Loan nextEntry = new Loan();

            nextEntry.setId(rset.getInt(1));
            nextEntry.setBorrower(rset.getInt(2));
            nextEntry.setBookID(rset.getString(3));
            nextEntry.setBorrowDate(rset.getDate(4).toString());
            nextEntry.setReturnDate(rset.getDate(5).toString());
            nextEntry.setReturned(rset.getBoolean(6));

            requestedList.add(nextEntry);
        }

        return requestedList;
    }
}
