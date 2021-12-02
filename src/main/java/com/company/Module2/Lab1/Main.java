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
        String xml = "src/main/java/com/company/Module2/Lab1/videoStore.xml";
        String xsd = "src/main/java/com/company/Module2/Lab1/mapMovieStore.xsd";

        MovieStore videoStore = new MovieStore(xml, xsd);

        List<Genre> genres = new ArrayList<>(Arrays.asList(new Genre(1,"Horror"),
                new Genre(2,"Comedy"),
                new Genre(3,"Thriller"),
                new Genre(4,"Fantasy")));
//
        genres.forEach(genre -> videoStore.addGenre(genre));

//        videoStore.addFilm(new Film(1, "Friday 13", 1.56f, genres.get(0)));
//        videoStore.addFilm(new Film(2, "Friends", 2.34f, genres.get(1)));
//        videoStore.addFilm(new Film(3, "Lord of the Rings", 1.2f, genres.get(3)));
//        videoStore.addFilm(new Film(4, "Very scary film", 1.2f, genres.get(0)));

//        videoStore.printCurrentState("Before modifying");
//        videoStore.deleteFilm(1);
//        videoStore.deleteGenre(3);
//        videoStore.editFilm(3, new Film(3, "Dumb & Dumber", 2.34f, videoStore.getGenre(2)));
//        videoStore.printCurrentState("After modifying");

        videoStore.saveToFile(xml);
    }

    public static void log(String message) {
        logger.log(Level.INFO, message);
    }
}
