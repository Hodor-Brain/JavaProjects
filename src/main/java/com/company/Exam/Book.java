package com.company.Exam;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String name;
    private String writer;
    private String publisher;
    private int publishingYear;
    private int numberOfPages;
    private int price;
    private String binding;

    public Book(int id, String name, String writer, String publisher, int publishingYear,
                int numberOfPages, int price, String binding){
        this.id = id;
        this.name = name;
        this.writer = writer;
        this.publisher = publisher;
        this.publishingYear = publishingYear;
        this.numberOfPages = numberOfPages;
        this.price = price;
        this.binding = binding;
    }

    public String toString(){
        return id + " " + name + ", by " + writer + ", published by " + publisher + " in " + publishingYear
                + ", " + numberOfPages + " pages, price " + price + ", the binding is " + binding + "\n";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWriter() {
        return writer;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getPrice() {
        return price;
    }

    public String getBinding() {
        return binding;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishingYear(int publishingYear) {
        this.publishingYear = publishingYear;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }
}
