/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.soen487librarysystem;

import com.christopheridah.soen487librarysystem.Exceptions.*;
import com.christopheridah.soen487librarycore.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import com.christopheridah.LibraryDataAccessLayer.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
        
import java.sql.ResultSet;

/**
 *
 * @author chris
 */

public class Library {

    private static Library instance = null;
    
    //private static HashMap<Integer, Book> currentStock;
    private static int currentBookID = 0;
    private BookDataGateway bookTDG;
   

    //CRUD
    private Library(){
        //currentStock = new HashMap<Integer, Book>();
        try{
            bookTDG = new BookDataGateway(); 
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
       
    public static Library getInstance(){
        
        if(instance == null){
            instance = new Library();
        }
        
        return instance;
   
    }
    
    public void addBook(String title, String description, String isbn, Author author, Publisher publisher) throws FailedDbConnException, InvalidAuthorException, InvalidPublisherException, OtherDbException {
           
            Book potentialBook = new Book(currentBookID, title, description, isbn, author, publisher);
            if (bookTDG != null){
                bookTDG.insertBook(generateID(), potentialBook);
            }
            else{
                throw new FailedDbConnException();
            
            //}catch(SQLException e){
              //  System.out.println("SQLState = " + e.getSQLState());
            //}
               
            }
    }
    
    public void addBook(Book book) throws FailedDbConnException, InvalidAuthorException, InvalidPublisherException, OtherDbException {
        addBook(book.getTitle(), book.getDescription(),book.getIsbn(),book.getAuthor(),book.getPublisher());
    }
    

    public Book getBook(int bookID) throws BookNotFoundException , OtherDbException, FailedDbConnException{
        String info = "info string";
        if (bookTDG != null){
            return bookTDG.getBook(bookID);
        } else{
            throw new FailedDbConnException();
        }
                
    }
    
    
    
    public void updateBook(int bookID, String title, String description, String ISBN, Author author, String publisherName) throws InvalidAuthorException, InvalidPublisherException, FailedDbConnException, OtherDbException   {
        if(bookTDG != null){
                   
            if(title == null || title.equals("")){
                title = null;
            }

            if(description == null || description.equals("")){
                description = null;
            }

            if(ISBN == null || ISBN.equals("")){
                ISBN = null;
            }

            if(author == null){
                author = null;
            }
            
            bookTDG.updateBook(bookID, title, description, ISBN, author, publisherName);

            if(publisherName == null || publisherName.equals("")){
                publisherName = null;
            }
        }
        else{
            throw new FailedDbConnException();
        }
        
          
    }


    public void deleteBook(int id) throws BookNotFoundException , OtherDbException, FailedDbConnException{
  
       if(bookTDG != null){ 
            bookTDG.deleteBook(id);
       }
       else{
           throw new FailedDbConnException();
       }
    }
    
   /*
    private boolean bookExists(int id) throws SQLException{
        
        Book book = bookTDG.getBook(id);
        return true;        
    }*/
 
    
    private int generateID(){
        int id = currentBookID;
        
        id = currentBookID + 1;  
        return id;
    }
    
    public String testString(){
        return "Test String";
    }
}
