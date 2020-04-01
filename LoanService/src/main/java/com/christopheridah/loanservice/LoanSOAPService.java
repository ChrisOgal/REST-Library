/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loanservice;

import com.christopheridah.loansystem.*;
import java.time.LocalDate;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 *
 * @author chris
 */
@WebService(serviceName = "LoanSOAPService")
@SOAPBinding(style = Style.RPC)
@Stateless()
public class LoanSOAPService {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "listAllMembers")
    public String listAllMembers() {

        return MemberManager.getInstance().getAllMembers();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getMemberInfo")
    public String getMemberInfo(@WebParam(name = "id") String id) {

        int memberID = Integer.parseInt(id);

        return MemberManager.getInstance().getMemberInfo(memberID);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addMember")
    public String addMember(@WebParam(name = "name") String name, @WebParam(name = "email") String email) {

        int outcome = MemberManager.getInstance().addNewMember(name, email);

        switch (outcome) {
            case 0: {
                return "Error encountered. Member not added";
            }

            default: {
                return "Member added. Your ID is " + outcome;
            }
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateMember")
    public String updateMember(@WebParam(name = "id") String id, @WebParam(name = "name") String name, @WebParam(name = "email") String email) {

        int memberID = Integer.parseInt(id);
        

        return MemberManager.getInstance().updateMember(memberID, name, email) + getMemberInfo(id);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteMember")
    public String deleteMember(@WebParam(name = "id") String id) {

        int deleteID = Integer.parseInt(id);

        int outcome = MemberManager.getInstance().deleteMember(deleteID);

        switch (outcome) {
            case 0: {
                return "Unable to delete member " + deleteID;
            }

            default: {
                return "Member " + deleteID + " has been deleted";
            }
        }

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "listBookLoans")
    public String listBookLoans(@WebParam(name = "bookID") String bookID) {

        return LoanManager.getInstance().getBookLoans(bookID);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "listMemberLoans")
    public String listMemberLoans(@WebParam(name = "memberID") String memberID) {

        return LoanManager.getInstance().getMemberLoans(Integer.parseInt(memberID));
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "returnBook")
    public String returnBook(@WebParam(name = "bookID") String bookID) {

        LoanManager.getInstance().returnBook(bookID);

        return "Book returned";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteLoan")
    public String deleteLoan(@WebParam(name = "loanID") String loanID) {

        int dirtyLoan = Integer.parseInt(loanID);
        int outcome = LoanManager.getInstance().deleteLoan(dirtyLoan);
        switch (outcome) {
            case 0:
                return "Unable to delete loan " + dirtyLoan + ".";

            default:
                return "Loan " + dirtyLoan + " has been deleted";

        }

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "createLoan")
    public String createLoan(@WebParam(name = "memberID") String memberID, @WebParam(name = "bookID") String bookID) {

        int newLoan = LoanManager.getInstance().borrowBook(Integer.parseInt(memberID), bookID);

        switch (newLoan) {
            case 0:
                return "Error. Loan not created.";

            default:
                return "Loan " + newLoan + " has been created.";
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateLoan")
    public String updateLoan(@WebParam(name = "loanID") String loanID, @WebParam(name = "memberID") String memberID,
            @WebParam(name = "bookID") String bookID, @WebParam(name = "borrowDate") String borrowDate,
            @WebParam(name = "returnDate") String returnDate, @WebParam(name = "returned") String returned) {

        LoanManager.getInstance().editLoan(Integer.parseInt(loanID), Integer.parseInt(memberID), bookID, LocalDate.parse(borrowDate),
                LocalDate.parse(returnDate), Boolean.valueOf(returned));

        return "Loan Updated.:\n" + LoanManager.getInstance().getLoan(Integer.parseInt(loanID));
    }

}
