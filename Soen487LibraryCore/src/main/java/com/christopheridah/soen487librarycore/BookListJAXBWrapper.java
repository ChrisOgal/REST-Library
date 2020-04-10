/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.christopheridah.soen487librarycore;

     
import java.util.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "bookList")
@XmlAccessorType (XmlAccessType.FIELD)
public class BookListJAXBWrapper{
    
    @XmlElement(name = "book")
    private List<Book> books;
 
    public BookListJAXBWrapper() {
        books = new ArrayList<Book>();
    }
 
    public BookListJAXBWrapper(List<Book> items) {
        this.books = items;
    }
 
    @XmlAnyElement(lax=true)
    public List<Book> getItems() {
        return books;
    }
    
    
 
}
