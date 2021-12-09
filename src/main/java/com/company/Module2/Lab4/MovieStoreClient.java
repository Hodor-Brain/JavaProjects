package com.company.Module2.Lab4;

import com.company.Module2.Lab2.Film;
import com.company.Module2.Lab2.Genre;
import com.company.Module2.Lab4.MovieStoreRemote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MovieStoreClient {
    public static void main(String[] args) throws RemoteException {
        MovieStoreRemote movieStore = null;
        try {
            movieStore = (MovieStoreRemote) Naming.lookup("//" + "localhost" + "/MovieStoreServer");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        for(var genre : movieStore.getGenres()) {
            System.out.println(genre);

            for(var film : movieStore.getFilmsByGenre(genre.getName())) {
                System.out.println(film);
            }
        }

        movieStore.addGenre(new Genre("New Genre"));
        movieStore.addFilm(new Film("EveryBody", 2.3f, "New Genre"));
        //movieStore.deleteFilm("EveryBody");
        movieStore.updateFilm(new Film("EveryBody"), new Film("None", 2.6f, "Comedy"));

        System.out.println("\n");

        for(var genre : movieStore.getGenres()) {
            System.out.println(genre);

            for(var film : movieStore.getFilmsByGenre(genre.getName())) {
                System.out.println(film);
            }
        }
    }
}
