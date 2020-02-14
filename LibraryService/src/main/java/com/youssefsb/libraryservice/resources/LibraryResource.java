/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.youssefsb.libraryservice.resources;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.MediaType;

import com.christopheridah.soen487librarysystem.Library;

/**
 * REST Web Service
 *
 * @author youssefsb
 */
@Path("Library")
@RequestScoped
public class LibraryResource {
    
    Library library;
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LibraryResource
     */
    public LibraryResource() {
        library = Library.getInstance();
    }

    /**
     * Retrieves representation of an instance of com.youssefsb.libraryservice.resources.GenericResource
     * @return an instance of java.lang.String
     */
    @Path("bookById")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getBook(@QueryParam("ID") int id) {
        
        return library.getInfo(id);
    
    }
    
    @Path("allBooks")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getBooksList() {
        
        String str = library.getStockAsString();
        
        return str;
    }
    
    @Path("addBook")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addBook(@FormParam("title") String title,
                          @FormParam("description") String desc,
                          @FormParam("isbn") String isbn,
                          @FormParam("Author") String author,
                          @FormParam("Publisher") String publisher){
        
        int bookID = library.addBook(title,desc,isbn,author,publisher);
        
        return "added book number: " + bookID + "\n";
    }
    
    /**
     * POST method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @Path("updateBook")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String updateBook(@QueryParam("id") int id,
                          @QueryParam("title") String title,
                          @QueryParam("description") String desc,
                          @QueryParam("isbn") String isbn,
                          @QueryParam("Author") String author,
                          @QueryParam("Publisher") String publisher){
        
        return library.updateBook(id, title, desc, isbn, author, publisher);
        
    }
    
    @Path("deleteBook/{id}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteBook(@PathParam("id") int id){
        return library.deleteBook(id);
    }
    
  
}
