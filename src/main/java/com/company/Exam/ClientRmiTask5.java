package com.company.Exam;

import com.company.Module2.Lab4.MovieStoreRemote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientRmiTask5 {
    public static void main(String[] args) throws RemoteException{
        BooksRemote booksRemote = null;
        try {
            booksRemote = (BooksRemote) Naming.lookup("//127.0.0.1/BooksServer");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        System.out.println(booksRemote.getBooksByWriter("Ruslan"));
        System.out.println(booksRemote.getBooksByPublisher("Publisher"));
        System.out.println(booksRemote.getBooksAfterYear(2006));
    }
}
