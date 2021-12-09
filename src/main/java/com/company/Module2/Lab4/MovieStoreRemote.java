package com.company.Module2.Lab4;

import com.company.Module2.Lab2.Genre;
import com.company.Module2.Lab2.Film;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MovieStoreRemote extends Remote {
    void addGenre(Genre genre) throws RemoteException;

    void deleteGenre(String name) throws RemoteException;

    void addFilm(Film film) throws RemoteException;

    void deleteFilm(String name) throws RemoteException;

    void updateFilm(Film old, Film other) throws RemoteException;

    int countFilmsByGenre(String name) throws RemoteException;

    Film getFilmByName(String name) throws RemoteException;

    List<Film> getFilmsByGenre(String name) throws RemoteException;

    List<Genre> getGenres() throws RemoteException;
}

