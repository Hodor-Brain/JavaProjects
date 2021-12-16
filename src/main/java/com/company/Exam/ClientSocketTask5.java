package com.company.Exam;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSocketTask5 {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public ClientSocketTask5(String ip, int port) throws IOException {
        socket = null;

        while (true) {
            try {
                System.out.println("Connecting to server...");
                socket = new Socket(ip, port);
                System.out.println("Connected");
                break;
            } catch (IOException e) {
                int timeout = 5000;
                System.out.printf("Failed. Waiting %d ms...%n", timeout);
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    private boolean sendQuery(QueryType type, String argument) throws IOException {
        out.writeUTF(type.name());
        out.writeUTF(argument);

        return in.readInt() == 0;
    }

    public void getBooksByWriter(String writer) throws IOException{
        if (sendQuery(QueryType.GET_BY_WRITER, writer)){
            String result = in.readUTF();
            System.out.println(result);

            return;
        }

        throw new RuntimeException("Error while getting books by writer");
    }

    public void getBooksByPublisher(String publisher) throws IOException{
        if (sendQuery(QueryType.GET_BY_PUBLISHER, publisher)){
            String result = in.readUTF();
            System.out.println(result);

            return;
        }

        throw new RuntimeException("Error while getting books by publisher");
    }

    public void getBooksAfterYear(int year) throws IOException{
        if (sendQuery(QueryType.GET_AFTER_YEAR, String.valueOf(year))){
            String result = in.readUTF();
            System.out.println(result);

            return;
        }

        throw new RuntimeException("Error while getting books by publisher");
    }

    public void disconnect() throws IOException {
        System.out.println("Disconnected from server");
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        ClientSocketTask5 client = null;
        try {
            client = new ClientSocketTask5("localhost", 2710);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.getBooksByWriter("Ruslan");
        client.getBooksByPublisher("Publisher");
        client.getBooksAfterYear(2006);

        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
