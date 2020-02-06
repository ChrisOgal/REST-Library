/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.soen487librarysystem;

import com.christopheridah.soen487restentities.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chris
 */
public class Library {

    private List<Book> currentStock;
    private int currentBookID = 0;
    private final Object lock = new Object();

    //CRUD
    public Library() {
        currentStock = new ArrayList<>();

    }

    public Library(List<Book> currentStock) {
        this.currentStock = currentStock;
    }

    public List<Book> listStock() {
        return this.currentStock;
    }
    
    public void clearStock()
    {
        currentStock.clear();
    }
    public int addBook(String title, String description, String isbn, Author author, Publisher publisher) {
        int bookID = 0;

        if (!bookExists(isbn)) {
            bookID = generateID();

            if (bookID > 0) {
                Book potentialBook = new Book(bookID, title, description, isbn, author, publisher);

                currentStock.add(potentialBook);
            }
        }

        return bookID;
    }

    public String getInfo(int bookID) {
        String info = "";

        if (bookExists(bookID)) {

            Book wanted = currentStock.get(bookID);
            info = "ID: " + wanted.getId()
                    + "\nTitle: " + wanted.getTitle()
                    + "\nDescription: " + wanted.getDescription()
                    + "\nISBN: " + wanted.getIsbn()
                    + "\nAuthor: " + wanted.getAuthor().getFirstName() + wanted.getAuthor().getLastName()
                    + "\nPublisher: " + wanted.getPublisher().getName()
                    + "\nPublisher Address: " + wanted.getPublisher().getAddress();
        } else {
            info = "Book does not exist";
        }

        return info;
    }

    public void updateBook(int bookID, String title, String description, String isbn, Author author, Publisher publisher) {

        Book potentialBook = new Book(bookID, title, description, isbn, author, publisher);

        synchronized (lock) {
            if (bookExists(bookID)) {

                currentStock.set(bookID, potentialBook);

            }
        }

    }

    public void deleteBook(int bookID) {

        synchronized (lock) {
            if (bookExists(bookID)) {

                currentStock.remove(bookID);
            }
        }

    }

    public void deleteBook(String isbn) {
        
        synchronized (lock)
        {
            if (bookExists(isbn))
            {
                for (Book dirtyBook: currentStock)
                {
                    if (dirtyBook.getIsbn().equalsIgnoreCase(isbn))
                    {
                        currentStock.remove(dirtyBook);
                        break;
                    }
                }
            }
        }
    }

    private int generateID() {
        int id = 0;

        synchronized (lock) {

            id += 1;

            return id;
        }

    }

    private boolean bookExists(int bookID) {
        boolean exists = false;

        synchronized (lock) {
            for (Book current : currentStock) {
                if (bookID == current.getId()) {
                    exists = true;
                }
            }
        }
        return exists;
    }

    private boolean bookExists(String isbn) {
        boolean exists = false;

        synchronized (lock) {
            for (Book current : currentStock) {
                if (current.getIsbn().equalsIgnoreCase(isbn)) {
                    exists = true;
                }
            }
        }

        return exists;
    }
}
