package com.company.Module2.Lab2;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
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



//        System.out.println("Before");
//        movieStore.showGenres();
//        movieStore.deleteGenre("Action");
//        movieStore.updateGenre("Fantasy", new Genre("Documentary"));
//
//        System.out.println("After");
//        movieStore.showGenres();

//        movieStore.showGenres();
//        movieStore.showFilms();

//        Genre documentary =  new Genre(2, "Documentary");

//        movieStore.addGenre(documentary);

//        movieStore.addFilm(new Film("Harry", 1.4f, documentary));
//        movieStore.addFilm(new Film("Harry1", 1.4f, documentary));
//        movieStore.addFilm(new Film("Harry2", 1.4f, documentary));

        // movieStore.deleteGenre("Documentary");
        movieStore.showGenres();
        movieStore.showFilms();

//        System.out.println( movieStore.getNumberOfFilmsByGenre("Documentary"));
//
//        for(Film film : movieStore.getFilmsByGenre("Documentary")) {
//            System.out.println(film);
//        }
//
//        movieStore.showGenres();

        movieStore.stop();
    }
}
