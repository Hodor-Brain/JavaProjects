package com.company.Exam;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRmiTask5 extends UnicastRemoteObject implements BooksRemote {
    private final BooksDAO booksDAO;

    public ServerRmiTask5(BooksDAO booksDAO) throws Exception{
        this.booksDAO = booksDAO;
    }

    @Override
    public List<Book> getBooksByWriter(String writer) throws RemoteException {
        return booksDAO.getBooksByWriter(writer);
    }

    @Override
    public List<Book> getBooksByPublisher(String publisher) throws RemoteException {
        return booksDAO.getBooksByPublisher(publisher);
    }

    @Override
    public List<Book> getBooksAfterYear(int year) throws RemoteException {
        return booksDAO.getBooksAfterYear(year);
    }

    public static void main(String[] args) throws Exception {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);

            List<Book> list = new ArrayList<>();
            list.add(new Book(1, "First book", "Ruslan", "Publisher",
                    2005, 250, 500, "web"));
            list.add(new Book(1, "Second book", "Ruslan", "Publisher",
                    2008, 100, 300, "web"));
            list.add(new Book(1, "Third book", "Ruslan", "Publisher",
                    2010, 500, 900, "web"));

            BooksDAO booksDAO = new BooksDAO(list);

            ServerRmiTask5 server = new ServerRmiTask5(booksDAO);

            reg.rebind("BooksServer", server);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
