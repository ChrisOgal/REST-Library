/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.youssefsb.libraryservice.resources;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Request;
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


import java.sql.SQLException;

import com.christopheridah.soen487librarysystem.Exceptions.*;

import com.christopheridah.soen487librarysystem.Library;

import com.christopheridah.soen487librarycore.* ;


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
    public LibraryResource()  {
   
            library = Library.getInstance();
     
    }

    /**
     * Retrieves representation of an instance of com.youssefsb.libraryservice.resources.GenericResource
     * @return an instance of java.lang.String
     */
    @Path("getBook")
    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response getBook(@QueryParam("ID") int id) {
        
        
        try{
            Book book = library.getBook(id); 
            return Response.status(200)
                .entity(book).build();
        }catch(BookNotFoundException e){
            return Response.status(404).entity("Book does not exist").build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    }
        //return Response.status(404).entity("Book does not exist").build();
  
  
    
    /*
    @Path("allBooks")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getBooksList() {
        
        //String str = library.getStockAsString();
        
        //return str;
    }*/
    
    
    
    //public String addBook(@FormParam("title") String title,
      //                    @FormParam("description") String desc,
        //                  @FormParam("isbn") String isbn,
          //                @FormParam("author") String author,
            //              @FormParam("publisher") String publisher){
    @Path("addBook")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(Book book){
        try{
            library.addBook(book);
            return Response.status(200)
                .entity(book).build();
        }catch(InvalidPublisherException e){
            return Response.status(400).entity("Invalid Publisher").build();
        }catch(InvalidAuthorException e){
            return Response.status(400).entity("Invalid Author").build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    
    }
    /**
     * POST method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
 
    @Path("updateBook")
    @PUT
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response updateBook(@QueryParam("id") int id,
                          @QueryParam("title") String title,
                          @QueryParam("description") String desc,
                          @QueryParam("isbn") String isbn,
                          @QueryParam("author") String author,
                          @QueryParam("publisher") String publisher){
        
        Author auth = new Author(author , "lastName");
        try{
            library.updateBook(id, title, desc, isbn, auth, publisher);
            return Response.status(400).entity("Invalid Publisher").build();
        }catch(InvalidPublisherException e){
            return Response.status(400).entity("Invalid Publisher").build();
        }catch(InvalidAuthorException e){
            return Response.status(400).entity("Invalid Author").build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    }

    
    
    @Path("deleteBook/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response deleteBook(@PathParam("id") int id){
        try{
            library.deleteBook(id);
            return Response.status(500).entity("Deleted book succefully").build();
        }catch(BookNotFoundException e){
            return Response.status(500).entity("Book does not exist").build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    }
  
}
