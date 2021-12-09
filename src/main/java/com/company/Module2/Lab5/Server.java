package com.company.Module2.Lab5;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.company.Module2.Lab2.Film;
import com.company.Module2.Lab2.Genre;
import com.company.Module2.Lab3.QueryType;
import com.company.Module2.Lab2.MovieStoreDAO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Server {
    private static final String splitter = "#";
    private final MovieStoreDAO movieStore;
    private Connection connection;
    private ServerSocket server;
    private Socket clientSocket;
    private List<String> currentArguments;
    private String response;

    public Server(MovieStoreDAO movieStore) {
        this.movieStore = movieStore;
    }

    public void start() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();

        Channel channelFromClient = connection.createChannel();
        Channel channelToClient = connection.createChannel();
        channelFromClient.queueDeclare("fromClient", false, false, false, null);
        channelToClient.queueDeclare("toClient", false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String query = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println(" [Server] Received '" + query + "'\n");
            String response = processQuery(query);

            channelToClient.basicPublish("", "toClient", null, response.getBytes(StandardCharsets.UTF_8));
        };
        channelFromClient.basicConsume("fromClient", true, deliverCallback, consumerTag -> {
        });

    }

    private String processQuery(String query) throws IOException {
        response = "Nothing";
        try {
            currentArguments = new ArrayList<>(Arrays.stream(query.split(splitter)).toList());

            QueryType type = QueryType.valueOf(getFirstElement());

            switch (type) {
                case ADD_GENRE -> movieStore.addGenre(new Genre(getFirstElement()));
                case DELETE_GENRE -> movieStore.deleteGenre(getFirstElement());
                case ADD_FILM -> addFilm();
                case UPDATE_FILM -> updateFilm();
                case DELETE_FILM -> deleteFilm();
                case COUNT_FILMS_BY_GENRE -> countFilmsByGenre(response);
                case GET_FILM_BY_NAME -> getFilmByName(response);
                case GET_FILMS_BY_GENRE -> getFilmsByGenre(response);
                case GET_GENRES -> getGenres(response);
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.toString(1);
        }
    }

    private String getFirstElement() {
        String element = currentArguments.get(0);
        currentArguments.remove(0);

        return element;
    }

    private List<String> getAllElements(int size) {
        List<String> list = new ArrayList<>(currentArguments.subList(0, size));
        for (int i = 0; i < size; i++) {
            currentArguments.remove(0);
        }

        return list;
    }

    private void addToResponse(String argument) {
        response += splitter + argument;
    }

    private void getGenres(String response) throws IOException {
        List<Genre> genres = movieStore.getGenres();

        addToResponse(Integer.toString(genres.size()));

        for (Genre genre : genres) {
            for (String argument : genre.toList())
                addToResponse(argument);
        }
    }

    private void getFilmsByGenre(String response) throws IOException {
        List<Film> films = movieStore.getFilmsByGenre(new Genre(getAllElements(Genre.listSize())).getName());

        addToResponse(Integer.toString(films.size()));

        for (Film film : films) {
            for (String argument : film.toList())
                addToResponse(argument);
        }
    }

    private void getFilmByName(String response) throws IOException {
        List<String> filmList = movieStore.getFilmByName(new Film(getAllElements(Film.listSize())).getName()).toList();

        for (int i = 0, n = Film.listSize(); i < n; i++) {
            addToResponse(filmList.get(i));
        }
    }

    private void countFilmsByGenre(String response) throws IOException {
        addToResponse(Integer.toString(movieStore.getNumberOfFilmsByGenre(new Genre(currentArguments.subList(0,
                Genre.listSize())).getName())));
    }

    private void updateFilm() {
        try{
            movieStore.updateFilm(new Film(getAllElements(Film.listSize())),
                    new Film(getAllElements(Film.listSize())));
        }
        catch (Exception exception){};
    }

    private void deleteFilm() {
        movieStore.deleteFilm(new Film(getAllElements(Film.listSize())).getName());
    }

    private void addFilm() {
        movieStore.addFilm(new Film(getAllElements(Film.listSize())));
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(new MovieStoreDAO());
        server.start();
    }
}
