/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.christopheridah.LibraryDataAccessLayer;

import com.christopheridah.soen487librarysystem.Exceptions.*;
import com.christopheridah.soen487librarycore.*;

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
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author youssefsb
 */
public class BookDataGateway {
   
    private static final String databaseURL = "jdbc:mysql://root:password@localhost:3306/Library?serverTimezone=UTC";
    private static Connection conn;
    //jdbc:mysql://127.0.0.1:3306/?user=root  
    
    public BookDataGateway() throws SQLException{
        try {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
        
        conn = DriverManager.getConnection(databaseURL);
        System.out.println("Connection established");
        
    }
    
    
    public Book getBook(int id)  throws BookNotFoundException, OtherDbException{
       
        String bookQuery = "select * from Book where BookId = ? ;";
        try{
            PreparedStatement ps = conn.prepareStatement(bookQuery);
            ps.setInt(1, id);
            ResultSet rset = ps.executeQuery(); 
            if (rset.next()) {
                Author auth = new Author(rset.getString("AuthorFirstName"), rset.getString("AuthorLastName"));
                Publisher publisher = new Publisher();
                publisher.setName(rset.getString("Publisher"));
                Book book = new Book(rset.getInt("BookId"),
                        rset.getString("Title"),
                        rset.getString("Description"),
                        rset.getString("ISBN"),
                        auth,
                        publisher);
            return book;
            } else {
                throw new BookNotFoundException();
            }
        }catch(SQLException e){
            throw new OtherDbException();
        }
    }
    
    
    public void insertBook(int bookId, Book book) throws InvalidAuthorException , OtherDbException, InvalidPublisherException{
      try{
        String insertStatement = "INSERT INTO Book VALUES (?,?,?,?,?,?,?);";
        PreparedStatement ps = conn.prepareStatement(insertStatement);
        ps.setInt(1, book.getId());
        ps.setString(2, book.getDescription());
        ps.setString(3, book.getIsbn());
        ps.setString(4, book.getAuthor().getFirstName());
        ps.setString(5, book.getAuthor().getLastName());
        ps.setString(6, book.getTitle());
        ps.setString(7 , book.getPublisher().getName());      
        ps.executeUpdate(); 
        }catch(SQLException e){
            if (e.getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")){
                if(e.getMessage().indexOf("Author") != -1){
                    throw new InvalidAuthorException();
                }
                else if(e.getMessage().indexOf("Publisher") != -1){
                    System.out.println(e.getMessage());
                    throw new InvalidPublisherException();
                }
            }
            else{
                e.printStackTrace();
                throw new OtherDbException();
            }
        }
    };
         
   
    public int updateBook(int bookID, String title, String description, String ISBN, Author author, String publisherName) throws InvalidAuthorException , OtherDbException, InvalidPublisherException{
        
        String updateStatement = "UPDATE Book Set \n";
        
        if(description != null && !description.equals("")){
            updateStatement += "Description = \"" + description +"\",";
        }
        if(ISBN != null && !ISBN.equals("")){
            updateStatement += "ISBN = \"" + ISBN + "\",";
        }
        if(author != null){
            updateStatement += "AuthorFirstName  = \"" + author.getFirstName() + "\",";
            updateStatement += "AuthorLastName  = \"" + author.getLastName() + "\",";        
        }
        if(publisherName != null ){
            updateStatement += "Publisher = \"" + publisherName + "\",";
        }
        if(title != null && !title.equals("")){
            updateStatement += "Title = \"" + title + "\",";
        }
        
        updateStatement = updateStatement.substring(0, updateStatement.length() - 1);
        
        updateStatement += "\n where BookID =" + bookID + ";";
        

        try{
            PreparedStatement ps = conn.prepareStatement(updateStatement);
            return ps.executeUpdate();
        }catch(SQLException e){
            if (e.getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")){
                
                if(e.getMessage().indexOf("Author") != 1){
                   throw new InvalidAuthorException();
                }
                else if(e.getMessage().indexOf("Publisher") != 1){
                    throw new InvalidPublisherException();
                }
            }
            
            throw new OtherDbException();
        }
    }
    
    public int deleteBook(int id) throws OtherDbException {
        try{
            String deleteStatement = "DELETE FROM Book WHERE BookID = "+ id;
            PreparedStatement ps = conn.prepareStatement(deleteStatement);
            return ps.executeUpdate();
        }catch(SQLException e){
          System.out.println(e.getMessage());
          throw new OtherDbException();
        }
    }
        
           
}
