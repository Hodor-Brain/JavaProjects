package com.company.Module2.Lab1;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, SAXException {
        String xml = "src/main/java/com/company/Module2/Lab1/MovieStore.xml";
        String xsd = "src/main/java/com/company/Module2/Lab1/MovieStore.xsd";

        MovieStore movieStore = new MovieStore(xml, xsd);

//        List<Genre> genres = new ArrayList<>(Arrays.asList(new Genre(1,"Horror"),
//                new Genre(2,"Comedy"),
//                new Genre(3,"Thriller"),
//                new Genre(4,"Fantasy")));
//
//        genres.forEach(genre -> movieStore.addGenre(genre));
//
//        movieStore.addFilm(new Film(1, "Friday 13", 1.56f, genres.get(0)));
//        movieStore.addFilm(new Film(2, "Friends", 2.34f, genres.get(1)));
//        movieStore.addFilm(new Film(3, "Lord of the Rings", 1.2f, genres.get(3)));
//        movieStore.addFilm(new Film(4, "Very scary film", 1.2f, genres.get(0)));


//        movieStore.deleteFilm(1);
//        movieStore.deleteGenre(3);
        movieStore.editFilm(3, new Film(3, "Dumb and Dumber", 2.34f, movieStore.getGenre(2)));
//        movieStore.printCurrentState("After modifying");

        movieStore.saveToFile(xml);
    }

    public static void log(String message) {
        logger.log(Level.INFO, message);
    }
}
