/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.youssefsb.libraryserviceclient;

import java.util.Scanner;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.UniformInterfaceException;


/**
 *
 * @author youssefsb
 */
public class Application {
    
    String helpMessage = "This is the help message!";
    
    String mainMenu = "1-help\n2-list all books\n3-display book by id\n"
                + "4-add book\n5-update book\n6-delete book\n7-quit";
    
    LibraryClient client = new LibraryClient();
    
    
    public void helpHandler(){
        System.out.println(helpMessage);
    }
    
    public void listAllBooksHandler(){
        try{
            System.out.println(client.getBooksList());
        } catch(UniformInterfaceException e){
            System.out.println("Hmmm something went wrong with our server");
        }
    }
    
    public void displayBookHandler(){
        Scanner idScanner = new Scanner(System.in);
        int id = idScanner.nextInt();
        try{
            System.out.println(client.getBook(Integer.toString(id)));
        } catch(UniformInterfaceException e){
            System.out.println("Hmmm something went wrong with our server");
        }
    }
    public void updateBookHandler(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Book id?");
        int id = scanner.nextInt();
        System.out.println("Edit book title ?");
        String title = scanner.nextLine();
        System.out.println("Edit ISBN ?");
        String isbn = scanner.nextLine();
        System.out.println("Edit description ?");
        String description = scanner.nextLine();
        System.out.println("Edit Author ?");
        String author = scanner.nextLine();
        System.out.println("Edit Publisher ?");
        String publisher = scanner.nextLine();
        
        try{
            System.out.println(client.updateBook(id, title, isbn, description,author,publisher));
        }catch(UniformInterfaceException e){
            System.out.println("Hmmm something went wrong with our server");
        }
    }
    
    public void deleteBookHandler(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Book id?");
        int id = scanner.nextInt();
        try{
            System.out.println(client.deleteBook(id));
        } catch(UniformInterfaceException e){
            System.out.println("Hmmm something went wrong with our server");
        }
    }
    
    public void addBookHandler(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Book title ?");
        String title = scanner.nextLine();
        System.out.println("ISBN ?");
        String isbn = scanner.nextLine();
        System.out.println("Description ?");
        String description = scanner.nextLine();
        System.out.println("Author ?");
        String author = scanner.nextLine();
        System.out.println("Publisher ?");
        String publisher = scanner.nextLine();
        
        try{
            System.out.println(client.addBook(title, isbn, description,author,publisher));
        } catch(UniformInterfaceException e){
            System.out.println("Hmmm something went wrong with our server");
        }
    }
    
    
    
   
    
    public void start(){
        
        boolean quit = false;
        
        while(!quit){
            System.out.println("Welcome to the Library Client!\n---------------------------------");
            System.out.println(mainMenu);
            Scanner firstMenuScanner = new Scanner(System.in);
            int firstMenuChoice = firstMenuScanner.nextInt();
            switch (firstMenuChoice) {
                case 1:
                    helpHandler();
                    break;
                case 2:
                    listAllBooksHandler();
                    break;
                case 3:
                    displayBookHandler();
                    break;
                case 4:
                    addBookHandler();
                    break;
                case 5:
                    updateBookHandler();
                    break;
                case 6:
                    deleteBookHandler();
                    break;
                case 7:
                    quit = true;
                    break;
            }
        }
    }
}
