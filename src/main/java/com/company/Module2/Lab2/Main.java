package com.company.Module2.Lab2;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {
        MovieStoreDAO movieStore = new MovieStoreDAO();

        Genre comedy = new Genre("Comedy");
        Genre horror = new Genre("Horror");
        Genre fantasy = new Genre("Fantasy");
//
//        movieStore.addGenre(comedy);
//        movieStore.addGenre(horror);
//        movieStore.addGenre(fantasy);

        Film film1 = new Film("Dumb & Dumber", 2.2f, comedy);
        Film film2 = new Film("Friends", 2f, comedy);
        Film film3 = new Film("Ha-Ha", 1.45f, comedy);
        Film film4 = new Film("Friday 13", 1.9f, horror);
        Film film5 = new Film("Harry Potter", 1.5f, fantasy);

//        movieStore.addFilm(film1);
//        movieStore.addFilm(film2);
//        movieStore.addFilm(film3);
//        movieStore.addFilm(film4);
//        movieStore.addFilm(film5);

//        Film film = new Film("Extra film", 5f, new Genre("Horror"));
//        movieStore.addFilm(film);

//        Film film = movieStore.getFilmByName("Extra film");
//        Film newFilm = new Film("RENAMED", 2f, movieStore.getGenreById(movieStore.getGenreId("Fantasy")));
//////
//        movieStore.updateFilm(film, newFilm);

        movieStore.deleteFilm("RENAMED");

//        movieStore.deleteGenre("Comedy");



        movieStore.showGenres();
        movieStore.showFilms();


        movieStore.stop();
    }
}
