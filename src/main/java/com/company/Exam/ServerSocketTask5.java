package com.company.Exam;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSocketTask5 {
    private BooksDAO booksDAO;
    private ServerSocket server;

    public ServerSocketTask5(int port) {
        this.server = null;
        this.booksDAO = null;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            List<Book> list = new ArrayList<>();
            list.add(new Book(1, "First book", "Ruslan", "Publisher",
                    2005, 250, 500, "web"));
            list.add(new Book(1, "Second book", "Ruslan", "Publisher",
                    2008, 100, 300, "web"));
            list.add(new Book(1, "Third book", "Ruslan", "Publisher",
                    2010, 500, 900, "web"));

            booksDAO = new BooksDAO(list);

            while (true) {
                System.out.println("Waiting for a client ...");
                final Socket socket = server.accept();
                System.out.println("Client connected");
                new Thread(() -> {
                    try {
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                        while (processQuery(in, out)) ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Error >>     " + e.getMessage());
        }
    }

    private boolean processQuery(DataInputStream in, DataOutputStream out) throws IOException {
        try {
            QueryType type = QueryType.valueOf(in.readUTF());

            switch (type) {
                case GET_BY_WRITER -> {
                    out.writeInt(0);
                    out.writeUTF(booksDAO.getBooksByWriter(in.readUTF()).toString());;
                }
                case GET_BY_PUBLISHER -> {
                    out.writeInt(0);
                    out.writeUTF(booksDAO.getBooksByPublisher(in.readUTF()).toString());;
                }
                case GET_AFTER_YEAR -> {
                    out.writeInt(0);
                    out.writeUTF(booksDAO.getBooksAfterYear(Integer.parseInt(in.readUTF())).toString());
                }
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            out.writeInt(-1);
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocketTask5 server = new ServerSocketTask5(2710);

        server.start();
    }
}
