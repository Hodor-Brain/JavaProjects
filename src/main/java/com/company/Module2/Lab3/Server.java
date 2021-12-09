package com.company.Module2.Lab3;

import com.company.Module2.Lab2.Film;
import com.company.Module2.Lab2.Genre;
import com.company.Module2.Lab2.MovieStoreDAO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private MovieStoreDAO videoStore;

    private ServerSocket server;
    private Socket clientSocket;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public Server(int port, MovieStoreDAO videoStore) {
        this.server = null;
        this.out = null;
        this.in = null;
        this.videoStore = videoStore;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (true) {
            acceptClient();

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            while (processQuery()) ;
        }
    }

    private boolean processQuery() throws IOException {
        try {
            QueryType type = QueryType.valueOf(in.readUTF());

            switch (type) {
                case ADD_GENRE -> videoStore.addGenre(new Genre(in.readUTF()));
                case DELETE_GENRE -> videoStore.deleteGenre(in.readUTF());
                case ADD_FILM -> addFilm();
                case DELETE_FILM -> deleteFilm();
                case UPDATE_FILM -> updateFilm();
                case COUNT_FILMS_BY_GENRE -> countFilmsByGenre();
                case GET_FILM_BY_NAME -> getFilmByName();
                case GET_FILMS_BY_GENRE -> getFilmsByGenre();
                case GET_GENRES -> getGenres();
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            out.writeInt(-1);
            return false;
        }
    }

    private void getGenres() throws IOException {
        List<Genre> genres = videoStore.getGenres();

        out.writeInt(0);
        out.writeInt(genres.size());

        for (Genre genre : genres) {
            for (String argument : genre.toList())
                out.writeUTF(argument);
        }
    }

    private void getFilmsByGenre() throws IOException {
        List<Film> films = videoStore.getFilmsByGenre(Genre.parseGenre(in).getName());

        out.writeInt(0);
        out.writeInt(films.size());

        for (Film film : films) {
            for (String argument : film.toList())
                out.writeUTF(argument);
        }
    }

    private void getFilmByName() throws IOException {
        List<String> list = new ArrayList<>();

        out.writeInt(0);

        for (int i = 0, n = Film.listSize(); i < n; i++)
            list.add(in.readUTF());

        videoStore.getFilmByName(new Film(list).getName());
    }

    private void countFilmsByGenre() throws IOException {
        out.writeInt(0);
        out.writeInt(videoStore.getNumberOfFilmsByGenre(Genre.parseGenre(in).getName()));
    }

    private void updateFilm() throws Exception {
        out.writeInt(0);
        videoStore.updateFilm(Film.parseFilm(in), Film.parseFilm(in));
    }

    private void deleteFilm() throws IOException {
        out.writeInt(0);
        videoStore.deleteFilm(Film.parseFilm(in).getName());
    }

    private void addFilm() throws IOException {
        out.writeInt(0);
        videoStore.addFilm(Film.parseFilm(in));
    }

    private void acceptClient() {
        clientSocket = null;
        int attempts = 5;

        while (attempts > 0) {
            try {
                System.out.println("Waiting for a client...");
                clientSocket = server.accept();
                System.out.println("Client connected");
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
            --attempts;
        }
    }

    public static void main(String[] args) throws Exception {
        MovieStoreDAO movieStore = new MovieStoreDAO();
        Server server = new Server(2710, movieStore);

        server.start();

        movieStore.stop();
    }
}
