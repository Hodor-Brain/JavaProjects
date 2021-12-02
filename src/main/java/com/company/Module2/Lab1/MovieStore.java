package com.company.Module2.Lab1;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieStore {
    private List<Genre> genres;
    private List<Film> films;

    private Parser parser;

    public MovieStore(String filename, String mapFilename) throws IOException, SAXException {
        this.parser = new Parser(filename, mapFilename);
        this.genres = new ArrayList<>();
        this.films = new ArrayList<>();

        loadFromFile();
    }

    public void changeFile(String filename, String mapFilename) throws IOException, SAXException {
        parser = new Parser(filename, mapFilename);
    }

    public void saveToFile(String filename) {
        parser.saveToFile(genres, films, filename);
    }

    public void loadFromFile() {
        genres = parser.getGenres();
        films = parser.getFilms();
    }

    public int countGenres() {
        return genres.size();
    }

    public Genre getGenre(int id) {
        return genres.stream().filter(genre -> genre.getId() == id)
                .findFirst().orElse(null);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void deleteGenre(int id) {
        Genre genre = getGenre(id);
        if (genre != null) {
            genres.remove(genre);
        }
    }

    public void editGenre(int id, Genre newGenre) {
        Genre genre = getGenre(id);
        if (genre != null) {
            genres.remove(genre);
            addGenre(newGenre);
        }
    }

    public int countFilms() {
        return films.size();
    }

    public Film getFilm(int id) {
        return films.stream().filter(film -> film.getId() == id)
                .findFirst().orElse(null);
    }

    public void addFilm(Film film) {
        if (genres.contains(film.getGenre())) {
            films.add(film);
        }
    }

    public void deleteFilm(int id) {
        Film film = getFilm(id);
        if (film != null) {
            films.remove(film);
        }
    }

    public void editFilm(int id, Film newFilm) {
        Film film = getFilm(id);
        if (film != null) {
            films.remove(film);
            addFilm(newFilm);
        }
    }

    public void printCurrentState(String header) {
        System.out.println(header + ":");
        System.out.println("Films:");
        for (Film film : films) {
            System.out.println(film);
        }

        System.out.println("Genres:");
        for (Genre genre : genres) {
            System.out.println(genre);
        }
    }
}
