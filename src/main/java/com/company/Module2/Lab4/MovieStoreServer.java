package com.company.Module2.Lab4;

import com.company.Module2.Lab2.Film;
import com.company.Module2.Lab2.Genre;
import com.company.Module2.Lab2.MovieStoreDAO;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MovieStoreServer extends UnicastRemoteObject implements MovieStoreRemote {
    private final MovieStoreDAO movieStore;

    protected MovieStoreServer(MovieStoreDAO videoStore) throws Exception {
        this.movieStore = videoStore;
    }

    @Override
    public void addGenre(Genre genre) throws RemoteException {
        synchronized (movieStore){
            movieStore.addGenre(genre);
        }
    }

    @Override
    public void deleteGenre(String genre) throws RemoteException {
        synchronized (movieStore){
            movieStore.deleteGenre(genre);
        }
    }

    @Override
    public void addFilm(Film film) throws RemoteException {
        synchronized (movieStore){
            movieStore.addFilm(film);
        }
    }

    @Override
    public void deleteFilm(String name) throws RemoteException {
        synchronized (movieStore){
            movieStore.deleteFilm(name);
        }
    }

    @Override
    public void updateFilm(Film old, Film other) throws RemoteException {
        synchronized (movieStore){
            try{
                movieStore.updateFilm(old, other);
            }
            catch (Exception exception){;}
        }
    }

    @Override
    public int countFilmsByGenre(String name) throws RemoteException {
        synchronized (movieStore){
            return movieStore.getNumberOfFilmsByGenre(name);
        }
    }

    @Override
    public Film getFilmByName(String name) throws RemoteException {
        synchronized (movieStore){
            return movieStore.getFilmByName(name);
        }
    }

    @Override
    public List<Film> getFilmsByGenre(String name) throws RemoteException {
        synchronized (movieStore){
            return movieStore.getFilmsByGenre(name);
        }
    }

    @Override
    public List<Genre> getGenres() throws RemoteException {
        synchronized (movieStore){
            return movieStore.getGenres();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);

            reg.rebind("MovieStoreServer", new MovieStoreServer(new MovieStoreDAO()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
