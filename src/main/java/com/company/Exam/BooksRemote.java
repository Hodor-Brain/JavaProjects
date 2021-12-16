package com.company.Exam;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BooksRemote extends Remote {
    List<Book> getBooksByWriter(String writer) throws RemoteException;

    List<Book> getBooksByPublisher(String publisher) throws RemoteException;

    List<Book> getBooksAfterYear(int year) throws RemoteException;
}
