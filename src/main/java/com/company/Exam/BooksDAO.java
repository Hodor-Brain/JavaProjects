package com.company.Exam;

import java.util.ArrayList;
import java.util.List;

public class BooksDAO {
    List<Book> books;

    BooksDAO(List<Book> books){
        this.books = books;
    }

    public void AddBook(Book book){
        books.add(book);
    }

    public List<Book> getBooks(){
        return books;
    }

    public List<Book> getBooksByWriter(String writer){
        List<Book> list = new ArrayList<>();

        for (int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            if (book.getWriter().equals(writer)){
                list.add(book);
            }
        }

        return list;
    }

    public List<Book> getBooksByPublisher(String publisher){
        List<Book> list = new ArrayList<>();

        for (int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            if (book.getPublisher().equals(publisher)){
                list.add(book);
            }
        }

        return list;
    }

    public List<Book> getBooksAfterYear(int year){
        List<Book> list = new ArrayList<>();

        for (int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            if (book.getPublishingYear() > year){
                list.add(book);
            }
        }

        return list;
    }
}
