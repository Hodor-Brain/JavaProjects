package com.company.Module2.Lab2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Genre implements Serializable {
    private int id;
    private String name;

    public Genre(String name) {
        this.id = -1;
        this.name = name;
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(Element genreElement) {
        this.id = Integer.parseInt(genreElement.getAttribute("id"));
        this.name = genreElement.getAttribute("name");
    }

    public Genre(List<String> arguments) {
        this.id = Integer.parseInt(arguments.get(0));
        this.name = arguments.get(1);
    }

    public static int listSize() {
        return 2;
    }

    public static Genre parseGenre(DataInputStream in) throws IOException {
        List<String> list = new ArrayList<>();

        for (int i = 0, n = Genre.listSize(); i < n; i++)
            list.add(in.readUTF());

        return new Genre(list);
    }

    public List<String> toList() {
        return new ArrayList<>(List.of(Integer.toString(id), name));
    }

    @Override
    public String toString() {
        return "Genre ID:" + id + ", " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id == genre.id && Objects.equals(name, genre.name);
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Element getElement(Document document) {
        Element element = document.createElement("Genre");

        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("name", name);

        return element;
    }

    public String getName() {
        return name;
    }
}
