/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.soen487librarycore;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author chris
 */
@XmlRootElement
public class Book {
    
    private int id;
    private String title, description, isbn;
    private Author author;
    private Publisher publisher;

    public Book() {
    }

    public Book(int id, String title, String description, String isbn, Author author, Publisher publisher) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
    }
    
     public Book(int id, String title, String description, String isbn, String author, String publisher) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.author = new Author(author , "");
        this.publisher = new Publisher(publisher , "");
    }
     
    @XmlElement(name = "id") 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @XmlElement(name = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    @XmlElement(name = "author")
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @XmlElement(name = "publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    
    public String toString(){
        return "Book: " + Integer.toString(id) + "\ntitle: " + title + "\nISBN: " + isbn + "\ndescription: " + description + "\nAuthor: " + author.getFirstName() + "\nPublisher: " + publisher.getName();
    }
    
}
