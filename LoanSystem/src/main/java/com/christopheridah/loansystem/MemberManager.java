/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.loansystem;

import com.christopheridah.loancore.Member;
import com.christopheridah.loansystem.dataAccessLayer.MemberTableGateway;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author chris
 */
public class MemberManager {
    
    private static MemberManager instance = null;
    private static final Object lock = new Object();
    private MemberTableGateway gateway;
    
    public MemberManager () throws SQLException
    {
        gateway = new MemberTableGateway();
    }
    
    public static MemberManager getInstance  () throws SQLException
    {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new MemberManager ();
                }
            }
        }

        return instance;
    }
    
    //CRUD on Member Data
    
    public int addNewMember (String name) throws SQLException, LoanException
    {
        
        return gateway.addNewMember(name);
      
    }
    
    public Member getMemberInfo(int id) throws SQLException
    {
        
        return gateway.getMemberInfo(id);
        
    }
    
    public void updateMember (int id, String name) throws LoanException, SQLException
    {
        gateway.updateMember(id, name);
    }
    
    public int deleteMember (int id) throws SQLException, LoanException
    {
        return gateway.deleteMember(id);
    }
    
    public List <Member> getAllMembers () throws SQLException
    { 
        return  gateway.getAllMembers(); 
    }
    
    public int deleteAllMembers () throws SQLException, LoanException
    
    {
        return gateway.deleteAllMembers();
    }
    
    public boolean memberExists (int id) throws SQLException, SQLException
    {
        return gateway.memberExists(id);
    }
    
}
