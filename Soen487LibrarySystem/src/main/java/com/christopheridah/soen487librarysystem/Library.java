/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.soen487librarysystem;

import com.christopheridah.soen487librarycore.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author chris
 */

public class Library {

    private static Library instance = null;
    
    private static HashMap<Integer, Book> currentStock;
    private static int currentBookID = 0;
   

    //CRUD
    private Library() {
        currentStock = new HashMap<Integer, Book>();
    }
       
    public static Library getInstance(){
        
        if(instance == null){
            instance = new Library();
        }
        
        return instance;
   
    }
    
    private Library(HashMap<Integer,Book> currentStock) {
        this.currentStock = currentStock;
    }
    
    
    public HashMap<Integer,Book> listStock() {
        return this.currentStock;
    }
    
    public void clearStock()
    {
        currentStock.clear();
    }
    
    public String addBook(String title, String description, String isbn, String author, String publisher) {
        
               
        if (!bookExists(isbn)) {
            currentBookID++;
           
            Book potentialBook = new Book(currentBookID, title, description, isbn, author, publisher);

            currentStock.put(currentBookID, potentialBook);
            
            return "added book number: " + Integer.toString(currentBookID) + "\n";

        }

        else return "Book with same ISBN exists";
    }

    public String getInfo(int bookID) {
        String info = "";

        if (bookExists(bookID)) {

            Book wanted = currentStock.get(bookID);
            info = wanted.toString();
        } else {
            info = "Book does not exist";
        }

        return info;
    }
    
    public String getStockAsString(){
        String listStr = "";
        
        for(Book book: currentStock.values()){
            listStr += book.toString() + "\n";
        }
        
        return listStr;
    }

    public String updateBook(int bookID, String title, String description, String isbn, String author, String publisher) {
        
        //To retrieve old values in case of null parameters
        if (bookExists(bookID)) {
            Book oldBook = currentStock.get(bookID);
        
            // Author and Publisher instances to create from the string params
            Author auth;
            Publisher publ;

            if(title == null || title.equals("")){
                title = oldBook.getTitle();
            }

            if(description == null || description.equals("")){
                description = oldBook.getDescription();
            }

            if(isbn == null || isbn.equals("")){
                isbn = oldBook.getIsbn();
            }

            if(author == null || author.equals("")){
                author = oldBook.getAuthor().getFirstName();
            }
            

            if(publisher == null || publisher.equals("")){
                publisher = oldBook.getPublisher().getName();
            }
            
        
            Book potentialBook = new Book(bookID, title, description, isbn, author, publisher);

            currentStock.put(bookID, potentialBook);
            
            return " updated book: " + Integer.toString(bookID);
        }
        
        else return "book doesn't exist with id:" + Integer.toString(bookID);

    }

    public String deleteBook(int bookID) {
        
        if (bookExists(bookID)) {

            currentStock.remove(bookID);
            return "deleted book " + Integer.toString(bookID);
        }
            
        else return "book " + Integer.toString(bookID) + " does not exist";    
       
    }

    public void deleteBook(String isbn) {
        
        if (bookExists(isbn)) {
            for (Book dirtyBook : currentStock.values()) {
                if (dirtyBook.getIsbn().equalsIgnoreCase(isbn)) {
                    currentStock.remove(dirtyBook.getId());
                    break;
                }
            }
        }
     }
    

    
    private boolean bookExists(int bookID) {
        boolean exists = false;

        if (currentStock.containsKey(bookID)) {
            exists = true;
        }

        return exists;
    }

    private boolean bookExists(String isbn) {
        boolean exists = false;

        for (Book current : currentStock.values()) {
            if (current.getIsbn().equalsIgnoreCase(isbn)) {
                exists = true;
            }
        }
    
        return exists;
    }
    
    private int generateID(){
        int id = currentBookID;
        do{
            id = currentBookID + 1;
            
        } while(currentStock.get(id) != null);
        
        return id;
    }
}