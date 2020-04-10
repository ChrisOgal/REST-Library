/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.youssefsb.libraryservice.resources;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.servlet.ServletContext;
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
import javax.ws.rs.core.GenericEntity;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import java.io.StringWriter;
import java.io.File;


import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.JAXBException;

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
    
    @Context 
    private ServletContext servletContext;

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
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
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
    
     /**
     * Retrieves representation of an instance of com.youssefsb.libraryservice.resources.GenericResource
     * @return an instance of java.lang.String
     */
    @Path("getBookHtml")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getBookHtml(@QueryParam("ID") int id) throws JAXBException, TransformerException  {
        try{
            Book book = library.getBook(id); 
            JAXBContext jaxbContext = JAXBContext.newInstance(Book.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
     
            //Marshal the employees list in console
            StringWriter sw = new StringWriter();
            StringWriter result = new StringWriter();
            jaxbMarshaller.marshal(book, sw);
            String bookXsl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:template match=\"/\"> <html> <body> <h2>Book</h2> <table border=\"1\"> <tr bgcolor=\"#3aef27\"> <th>id</th> <th>title</th> <th>isbn</th> <th>description</th> <th>author</th> <th>publisher</th> </tr> <tr> <td><xsl:value-of select=\"book/id\"/></td> <td><xsl:value-of select=\"book/title\"/></td> <td><xsl:value-of select=\"book/isbn\"/></td> <td><xsl:value-of select=\"book/description\"/></td> <td><xsl:value-of select=\"book/author\"/></td> <td><xsl:value-of select=\"book/publisher\"/></td> </tr> </table> </body> </html> </xsl:template></xsl:stylesheet>";
            InputStream streamXsl = new ByteArrayInputStream(bookXsl.getBytes(StandardCharsets.UTF_8));
            InputStream streamXml = new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8));
            
            TransformerFactory factory = TransformerFactory.newInstance();
            StreamSource xslt = new StreamSource(streamXsl);
            Transformer transformer = factory.newTransformer(xslt);

            transformer.transform(new StreamSource(streamXml), new StreamResult(result));
            
            return Response.status(200)
                .entity(result.toString()).build();
        }catch(BookNotFoundException e){
            return Response.status(404).entity("Book does not exist").build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    }
    
    
        //return Response.status(404).entity("Book does not exist").build();
  
       
    //public String addBook(@FormParam("title") String title,
      //                    @FormParam("description") String desc,
        //                  @FormParam("isbn") String isbn,
          //                @FormParam("author") String author,
            //              @FormParam("publisher") String publisher){
    @Path("addBook")
    @POST
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response addBook(Book book){
        
        
        try{
            library.addBook(book);
            return Response.status(200)
                .entity("book added successfully").build();
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
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON,})
    public Response updateBook(@QueryParam("id") int id,
                          @QueryParam("title") String title,
                          @QueryParam("description") String desc,
                          @QueryParam("isbn") String isbn,
                          @QueryParam("author") String author,
                          @QueryParam("publisher") String publisher){
        
        Author auth = new Author(author , "");
        Publisher publ = new Publisher(publisher , "");
        try{
            library.updateBook(id, title, desc, isbn, auth, publ);
            return Response.status(200).entity("updated book successfully").build();
        }catch(BookNotFoundException e){
            return Response.status(400).entity("Book not found").build();
        }
        catch(InvalidPublisherException e){
            return Response.status(500).entity("Invalid Publisher").build();
        }catch(InvalidAuthorException e){
            return Response.status(500).entity("Invalid Author").build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    }

    @Path("updateBook")
    @PUT
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON,})
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON,})
    public Response updateBook(Book book){
        System.out.println("Book here:" + book.toString());
        try{
            library.updateBook(book.getId(), book.getTitle(), book.getDescription(), book.getIsbn(), book.getAuthor(), book.getPublisher());
            return Response.status(200).entity("updated book successfully").build();
        }catch(BookNotFoundException e){
            return Response.status(400).entity("Book not found").build();
        }
        catch(InvalidPublisherException e){
            return Response.status(500).entity("Invalid Publisher").build();
        }catch(InvalidAuthorException e){
            return Response.status(500).entity("Invalid Author").build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    }

    
    
    @Path("deleteBook/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Response deleteBook(@PathParam("id") int id){
        try{
            library.deleteBook(id);
            return Response.status(200).entity("Deleted book succefully").build();
        }catch(BookNotFoundException e){
            return Response.status(400).entity("Book does not exist").build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    }
    
    @Path("getAllBooks")
    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response getAllBooks(){
        try{
            List<Book> bookList  = library.getAllBooks(); 
            GenericEntity<List<Book>> entity = new GenericEntity<List<Book>>(bookList) {};
            return Response.status(200)
                .entity(entity).build();
        }catch (OtherDbException e){
            return Response.status(500).entity("Internal error. Could not process request.").build();
        }catch (FailedDbConnException e){
            return Response.status(500).entity("Failed to connect to Database").build();
        }
    }
    @Path("getAllBooksHTML")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getAllBooksHTML() throws JAXBException, TransformerException{
        try{
            List<Book> bookList  = library.getAllBooks(); 
            //GenericEntity<List<Book>> entity = new GenericEntity<List<Book>>(bookList) {};
            BookListJAXBWrapper wrapper = new BookListJAXBWrapper(bookList);
            JAXBContext jaxbContext = JAXBContext.newInstance(BookListJAXBWrapper.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
     
            //Marshal the employees list in console
            StringWriter sw = new StringWriter();
            StringWriter result = new StringWriter();
            jaxbMarshaller.marshal(wrapper, sw);
                        
            String bookXsl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:template match=\"/\"> <html> <body> <h2>All Books</h2> <table border=\"1\"> <tr bgcolor=\"#3aef27\"> <th>id</th> <th>title</th> <th>isbn</th> <th>description</th> <th>author</th> <th>publisher</th> </tr> <xsl:for-each select=\"bookList/book\"> <tr> <td><xsl:value-of select=\"id\"/></td> <td><xsl:value-of select=\"title\"/></td> <td><xsl:value-of select=\"isbn\"/></td> <td><xsl:value-of select=\"description\"/></td> <td><xsl:value-of select=\"author\"/></td> <td><xsl:value-of select=\"publisher\"/></td> </tr> </xsl:for-each> </table> </body> </html> </xsl:template></xsl:stylesheet>";
            InputStream streamXsl = new ByteArrayInputStream(bookXsl.getBytes(StandardCharsets.UTF_8));
            InputStream streamXml = new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8));
            
            TransformerFactory factory = TransformerFactory.newInstance();
            StreamSource xslt = new StreamSource(streamXsl);
            Transformer transformer = factory.newTransformer(xslt);

            transformer.transform(new StreamSource(streamXml), new StreamResult(result));
        

            return Response.status(200)
                    .entity(result.toString()).build();
        } catch (OtherDbException e) {
            return Response.status(500).entity("Internal error. Could not process request.").build();
        } catch (FailedDbConnException e
    
        ){
            return Response.status(500).entity("Failed to connect to Database").build();
    }
        
    }
}
