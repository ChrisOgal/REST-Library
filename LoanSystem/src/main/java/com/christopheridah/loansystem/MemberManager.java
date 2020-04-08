/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loansystem;

import com.christopheridah.loancore.Member;
import com.christopheridah.loansystem.dataAccessLayer.MemberTableGateway;
import org.codehaus.jackson.map.ObjectMapper;
import java.util.List;

/**
 *
 * @author chris
 */
public class MemberManager {

    private static MemberManager instance = null;
    private static final Object lock = new Object();
    private MemberTableGateway gateway;
    private ObjectMapper objectMapper = new ObjectMapper();

    public MemberManager() throws LoanException {

        try {

            gateway = new MemberTableGateway();
        } catch (Exception e) {
            
            throw new LoanException("", e);
        }
    }

    public static MemberManager getInstance() throws LoanException {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new MemberManager();
                }
            }
        }

        return instance;
    }

    //CRUD on Member Data
    public int addNewMember(String name, String email) throws LoanException {
        try {

            int result = gateway.addNewMember(name, email);
            return result;
        } catch (Exception e) {
           throw new LoanException("", e);
        }

    }

    public String getMemberInfo(int id) throws LoanException {

        try {
            Member requestedMember = gateway.getMemberInfo(id);

            return objectMapper.writeValueAsString(requestedMember);
        } catch (Exception e) {
            throw new LoanException("", e);
        }

    }

    public String updateMember(int id, String name, String email) throws LoanException {

        
        try {

            return gateway.updateMember(id, name, email);
        } catch (Exception e) {
                    throw new LoanException("", e);
        }

        
    }

    public int deleteMember(int id) throws LoanException {

        try {
            return gateway.deleteMember(id);
        } catch (Exception e) {
            throw new LoanException("", e);
        }
    }

    public String getAllMembers() throws LoanException {
        try {

            List<Member> membersList = gateway.getAllMembers();
            return objectMapper.writeValueAsString(membersList);
        } catch (Exception e) {
            
            throw new LoanException("", e);
        }

    }

    public int deleteAllMembers() throws LoanException {

        try {
            return gateway.deleteAllMembers();
        } catch (Exception e) {
           throw new LoanException("", e);
        }
    }

    public boolean memberExists(int id) throws LoanException {

        try {
            return gateway.memberExists(id);
        } catch (Exception e) {
           throw new LoanException("", e);
        }
    }

}
