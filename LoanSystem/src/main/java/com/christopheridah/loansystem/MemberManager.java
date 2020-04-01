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

    public MemberManager() {

        try {

            gateway = new MemberTableGateway();
        } catch (Exception e) {
            String reason = e.toString();
            String fake = "";
        }
    }

    public static MemberManager getInstance() {

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
    public int addNewMember(String name, String email) {
        try {

            int result = gateway.addNewMember(name, email);
            return result;
        } catch (Exception e) {
            return -4;
        }

    }

    public String getMemberInfo(int id) {

        try {
            Member requestedMember = gateway.getMemberInfo(id);

            return objectMapper.writeValueAsString(requestedMember);
        } catch (Exception e) {
            return e.toString();
        }

    }

    public String updateMember(int id, String name, String email) {

        String update = "";
        try {

            return gateway.updateMember(id, name, email);
        } catch (Exception e) {

        }

        return "Error updating";
    }

    public int deleteMember(int id) {

        try {
            return gateway.deleteMember(id);
        } catch (Exception e) {
            return -4;
        }
    }

    public String getAllMembers() {
        try {

            List<Member> membersList = gateway.getAllMembers();
            return objectMapper.writeValueAsString(membersList);
        } catch (Exception e) {
            return e.toString();
        }

    }

    public int deleteAllMembers() {

        try {
            return gateway.deleteAllMembers();
        } catch (Exception e) {
            return -4;
        }
    }

    public boolean memberExists(int id) {

        try {
            return gateway.memberExists(id);
        } catch (Exception e) {
            return false;
        }
    }

}
