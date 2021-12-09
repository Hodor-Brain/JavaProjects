package com.company.Module2.Lab3;

import com.company.Module2.Lab2.Film;
import com.company.Module2.Lab2.Genre;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public Client(String ip, int port) throws IOException {
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

    public boolean addGenre(Genre genre) throws IOException {
        return sendQuery(QueryType.ADD_GENRE, List.of(genre.getName()));
    }

    public boolean deleteGenre(Genre genre) throws IOException {
        return sendQuery(QueryType.DELETE_GENRE, List.of(genre.getName()));
    }

    public boolean addFilm(Film film) throws IOException {
        return sendQuery(QueryType.ADD_FILM, film.toList());
    }

    public boolean deleteFilm(Film film) throws IOException {
        return sendQuery(QueryType.ADD_FILM, film.toList());
    }

    public boolean updateFilm(Film oldFilm, Film newFilm) throws IOException {
        List<String> arguments = oldFilm.toList();
        arguments.addAll(newFilm.toList());

        return sendQuery(QueryType.UPDATE_FILM, arguments);
    }

    public int countFilmsByGenre(Genre genre) throws IOException {
        if (sendQuery(QueryType.COUNT_FILMS_BY_GENRE, List.of(genre.getName()))) {
            return in.readInt();
        }

        throw new RuntimeException("Error while counting genres");
    }

    public Film getFilmByName(Film film) throws IOException {
        if (sendQuery(QueryType.GET_FILM_BY_NAME, film.toList())) {
            Film resultFilm = Film.parseFilm(in);
            return resultFilm;
        }

        throw new RuntimeException("Error while getting film");
    }

    public List<Film> getFilmsByGenre(Genre genre) throws IOException {
        if (sendQuery(QueryType.GET_FILMS_BY_GENRE, genre.toList())) {
            int count = in.readInt();

            List<Film> films = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                films.add(Film.parseFilm(in));
            }

            return films;
        }

        throw new RuntimeException("Error while getting films by genre");
    }

    public List<Genre> getGenres() throws IOException {
        if (sendQuery(QueryType.GET_GENRES, List.of())) {

            int count = in.readInt();

            List<Genre> genres = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                genres.add(Genre.parseGenre(in));
            }

            return genres;
        }

        throw new RuntimeException("Error while getting genres");
    }

    public void disconnect() throws IOException {
        System.out.println("Disconnected from server");
        socket.close();
    }

    private boolean sendQuery(QueryType type, List<String> arguments) throws IOException {
        out.writeUTF(type.name());

        for (String argument : arguments) {
            out.writeUTF(argument);
        }

        return in.readInt() == 0;
    }

    public static void main(String[] args) throws IOException {
        Client client = null;
        try {
            client = new Client("localhost", 2710);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        String genreName;
        String filmName;
        float duration;



        while (true) {
            for (Genre genre : client.getGenres()) {
                System.out.println(genre);

                for(Film film : client.getFilmsByGenre(genre)) {
                    System.out.println(film);
                }
            }
//            System.out.print("Enter a genre: ");
//            genreName = sc.nextLine();
//
//            if (Objects.equals(genreName, "stop")) {
//                break;
//            }
//
//            client.addGenre(new Genre(genreName));

//            for (Genre genre : client.getGenres()) {
//                System.out.println(genre);
//            }

            System.out.print("Enter a film name: ");
            filmName = sc.nextLine();
//
            if (filmName == "stop")
                break;

            System.out.print("Enter a film duration: ");
            duration = Float.parseFloat(sc.nextLine());
            System.out.print("Enter a film genre: ");
            genreName = sc.nextLine();

            client.addFilm(new Film(filmName, duration, genreName));
        }

        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
