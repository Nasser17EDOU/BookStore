/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstore;

/**
 *
 * @author Tony NDOSS
 */
public class Book {
    String bookTitle = "";
    String bookGenre = "";
    String author = "";
    double price = 0;
    int stock = 0;
    
    public Book(String bookTitle, String bookGenre, String author, double price, int stock){
        this.bookTitle = bookTitle;
        this.bookGenre = bookGenre;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }
    
    public void setBookTitle(String bookTitle){
        this.bookTitle = bookTitle;
    }
    public String getBookTitle(){
        return bookTitle;
    }
    
    public void setBookGenre(String bookGenre){
        this.bookGenre = bookGenre;
    }
    public String getBookGenre(){
        return bookGenre;
    }
    
    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return author;
    }
    
    public void setPrice(double price){
        this.price = price;
    }
    public double getPrice(){
        return price;
    }
    
    public void setStock(int stock){
        this.stock = stock;
    }
    public int getStock(){
        return stock;
    }
}
