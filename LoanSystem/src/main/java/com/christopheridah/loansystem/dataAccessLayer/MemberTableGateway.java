/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loansystem.dataAccessLayer;

import com.christopheridah.loancore.Member;
import com.christopheridah.loansystem.LoanException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chris
 */
public class MemberTableGateway {

    private static String databaseURL = "jdbc:derby://localhost:1527/Library";
    private static Connection conn;
    private static final Object lock = new Object();

    public MemberTableGateway() throws SQLException, ClassNotFoundException, InstantiationException {

        conn = DriverManager.getConnection(databaseURL);
    }

    public int addNewMember(String name, String email) throws SQLException, LoanException {
        int confirmedMemberID = 0;

        int memberID = generateID();

        if (checkDuplicateId(memberID) > 0 || checkDuplicateMember(name, email) > 0) {
            throw new LoanException("Attempting to add a duplicate member.");
        } else {
            String insertMember = "INSERT INTO Member VALUES (?,?,?)";

            try (PreparedStatement ps = conn.prepareStatement(insertMember)) {

                ps.setInt(1, memberID);
                ps.setString(2, name);
                ps.setString(3, email);

                synchronized (lock) {
                    ps.executeUpdate();

                    String confirmID = "select MAX(id) from Member";

                    try (PreparedStatement ps2 = conn.prepareStatement(confirmID)) {
                        try (ResultSet rset = ps2.executeQuery()) {

                            rset.next();
                            confirmedMemberID = rset.getInt(1);
                        }
                    }
                }

            }

        }

        return confirmedMemberID;
    }

    public Member getMemberInfo(int id) throws SQLException {
        Member requestedMember = new Member();
        String query = "select * from Member where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rset = ps.executeQuery()) {
                rset.next();
                requestedMember.setId(rset.getInt(1));
                requestedMember.setName(rset.getString(2));
                requestedMember.setEmail(rset.getString(3));
            }
        }
        return requestedMember;

    }

    public String updateMember(int id, String name, String email) throws LoanException, SQLException {

        if (!memberExists(id)) {

            return "Could not update. Member does not exist";
        }

        if (checkDuplicateMember(name, email) > 0) {
            return "Member's information matches the requested updates";
        } else {
            Member dirtyMember = getMemberInfo(id);
            Member cleanMember = new Member(id, name, email);

            if (!dirtyMember.equals(cleanMember)) {
                String update = "update Member set memberName = ?, memberEmail = ? where id = ?";

                try (PreparedStatement ps = conn.prepareStatement(update)) {

                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setInt(3, id);
                    ps.executeUpdate();

                }
            }

        }
        return "Member updated";
    }

    public int deleteMember(int id) throws SQLException, LoanException {
        if (!memberExists(id)) {
            throw new LoanException("Member does not exist");
        } else {
            String deleteQuery = "delete from Member where id = ?";

            try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                ps.setInt(1, id);
                return ps.executeUpdate();
            }
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> memberList = new ArrayList<>();
        String query = "select * from Member";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rset = ps.executeQuery()) {
                while (rset.next()) {
                    Member nextEntry = new Member();

                    nextEntry.setId(rset.getInt(1));
                    nextEntry.setName(rset.getString(2));
                    nextEntry.setEmail(rset.getString(3));
                    memberList.add(nextEntry);
                }
            }
        }

        return memberList;

    }

    public int deleteAllMembers() throws SQLException, LoanException {
        int tableSize = 0;

        String getTableSize = "select MAX(id) from Member";

        try (PreparedStatement ps = conn.prepareStatement(getTableSize)) {
            try (ResultSet rset = ps.executeQuery()) {
                rset.next();

                tableSize = rset.getInt(1);

                for (int i = tableSize; i > 0; i--) {
                    deleteMember(i);
                    tableSize--;
                }

                return tableSize;
            }
        }
    }

    public boolean memberExists(int id) throws SQLException {

        String query = "select * from Member where id = ?";

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

    private int checkDuplicateId(int id) throws SQLException {

        int duplicate = 0;

        String duplicateTest = "select COUNT(id) from Member where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(duplicateTest)) {

            ps.setInt(1, id);

            try (ResultSet rset = ps.executeQuery()) {
                rset.next();

                duplicate = rset.getInt(1);
            }
        }

        return duplicate;
    }

    private int checkDuplicateMember(String name, String email) throws SQLException {
        int duplicate = 0;

        String duplicateTest = "select COUNT(*) from Member where memberName = ? and memberEmail = ?";

        try (PreparedStatement ps = conn.prepareStatement(duplicateTest)) {
            ps.setString(1, name);
            ps.setString(2, email);

            try (ResultSet rset = ps.executeQuery()) {
                rset.next();

                duplicate = rset.getInt(1);
            }

        }
        return duplicate;
    }

    private int generateID() throws SQLException {
        int additionID = 0;

        synchronized (lock) {
            String lastID = "Select MAX(id) from Member";

            try (PreparedStatement ps = conn.prepareStatement(lastID)) {

                try (ResultSet rset = ps.executeQuery()) {

                    rset.next();

                    additionID = rset.getInt(1) + 1;
                }
            }
        }

        return additionID;
    }
}
