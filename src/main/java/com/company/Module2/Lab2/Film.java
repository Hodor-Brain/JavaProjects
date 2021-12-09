package com.company.Module2.Lab2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Film implements Serializable {
    private int id;
    private String name;
    private Float duration;
    private Genre genre;

    public Film(String name) {
        this.id = -1;
        this.name = name;
        this.duration = -1f;
        this.genre = null;
    }

    public Film(String name, Float duration, String genreName) {
        this.id = -1;
        this.name = name;
        this.duration = duration;
        this.genre = new Genre(genreName);
    }

    public Film(int id, String name, Float duration, Genre genre) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.genre = genre;
    }

    public Film(String name, Float duration, Genre genre) {
        this.name = name;
        this.duration = duration;
        this.genre = genre;
    }

    public Film(List<String> arguments) {
        this.id = Integer.parseInt(arguments.get(0));
        this.name = arguments.get(1);
        this.duration = Float.parseFloat(arguments.get(2));
        this.genre = new Genre(arguments.subList(3, 5));
    }

    public Film (Element filmElement,Element genreElement) {
        this.id = Integer.parseInt(filmElement.getAttribute("id"));
        this.name = filmElement.getAttribute("name");
        this.duration = Float.parseFloat(filmElement.getAttribute("duration"));
        this.genre = new Genre(genreElement);
    }

    public static int listSize() {
        return Genre.listSize() + 3;
    }

    public static Film parseFilm(DataInputStream in) throws IOException {
        List<String> list = new ArrayList<>();

        for (int i = 0, n = Film.listSize(); i < n; i++)
            list.add(in.readUTF());

        return new Film(list);
    }

    public Genre getGenre() {
        return genre;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ID:" + id + ", '" + name + "', duration : " + duration + ", " + genre;
    }

    public Element getElement(Document document) {
        Element element = document.createElement("Film");

        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("name", name);
        element.setAttribute("duration", Float.toString(duration));

        return element;
    }

    public List<String> toList() {
        List<String> filmList = new ArrayList<>(List.of(Integer.toString(id), name, Float.toString(duration)));
        filmList.addAll(genre.toList());

        return filmList;
    }

    public Float getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }
}
