/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.youssefsb.libraryserviceclient;

import javax.ws.rs.core.Form;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


/**
 * Jersey REST client generated for REST resource:LibraryResource [Library]<br>
 * USAGE:
 * <pre>
 *        LibraryClient client = new LibraryClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author youssefsb
 */
public class LibraryClient {

    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/LibraryService-1.0";

    public LibraryClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("Library");
    }

    public String getBook(String ID) throws UniformInterfaceException {
        WebResource resource = webResource;
        if (ID != null) {
            resource = resource.queryParam("ID", ID);
        }
        resource = resource.path("bookById");
        return resource.accept(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public String updateBook(int id,String title, String isbn, String desc, String author, String publisher) throws UniformInterfaceException {
        MultivaluedHashMap formData = new MultivaluedHashMap();
        
        WebResource resource = webResource;
        formData.add("id" , Integer.toString(id));
        formData.add("title" , title);
        formData.add("description" , desc);
        formData.add("author" , author);
        formData.add("isbn" , isbn);
        formData.add("publisher" , publisher);
        
        resource = resource.queryParams(formData);
        
        return  resource.path("updateBook").type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).put(String.class);
    
    }

    public String getBooksList() throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path("allBooks");
        return resource.accept(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public String addBook(String title, String isbn, String desc, String author, String publisher) throws UniformInterfaceException {
        MultivaluedHashMap formData = new MultivaluedHashMap();

        formData.add("title" , title);
        formData.add("description" , desc);
        formData.add("author" , author);
        formData.add("isbn" , isbn);
        formData.add("publisher" , publisher);
        return  webResource.path("addBook").type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(String.class, formData);
    }
    
    public String deleteBook(int id) throws UniformInterfaceException {
        return webResource.path("deleteBook/" + Integer.toString(id)).delete(String.class);
    }

    public void close() {
        client.destroy();
    }
    
}
