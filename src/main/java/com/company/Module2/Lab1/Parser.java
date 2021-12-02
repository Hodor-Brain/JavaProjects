package com.company.Module2.Lab1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String MOVIE_STORE = "MovieStore";

    private Document doc;
    private Element root;

    public Parser(String filename, String mapFilename) throws IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        this.root = null;

        initializeValidation(mapFilename, dbf);
        this.doc = createDocument(filename, dbf);
        initializeRoot();
    }

    public List<Genre> getGenres() {
        List<Genre> genres = new ArrayList<>();
        NodeList genreList = getGenreList();

        if (genreList != null) {
            for (int i = 0, n = genreList.getLength(); i < n; i++) {
                Element genreElement = (Element) genreList.item(i);
                genres.add(new Genre(genreElement));
            }
        }

        return genres;
    }

    public List<Film> getFilms() {
        List<Film> films = new ArrayList<>();
        NodeList genreList = getGenreList();

        if (genreList != null) {
            for (int i = 0, n = genreList.getLength(); i < n; i++) {
                Element genreElement = (Element) genreList.item(i);
                NodeList filmList = genreElement.getElementsByTagName("Film");

                for (int j = 0, m = filmList.getLength(); j < m; j++) {
                    Element filmElement = (Element) filmList.item(j);
                    films.add(new Film(filmElement, genreElement));
                }
            }
        }
        return films;
    }

    public void saveToFile(List<Genre> genres, List<Film> films, String filename) {
        Document document = null;

        try {
            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Element newRoot = document.createElement(MOVIE_STORE);
        document.appendChild(newRoot);

        for (Genre genre : genres) {
            Element genreElement = genre.getElement(document);
            newRoot.appendChild(genreElement);

            for (Film film : films) {
                if (film.getGenre().equals(genre)) {
                    genreElement.appendChild(film.getElement(document));
                }
            }

        }


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;

        try {
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "WINDOWS-1251");
            transformer.transform(new DOMSource(document), new StreamResult(new File(filename)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private NodeList getGenreList() {
        if (root == null)
            return null;

        Element movieStore;

        if (root.getTagName().equals(MOVIE_STORE)) {
            movieStore = root;
        } else {
            movieStore = (Element) root.getElementsByTagName(MOVIE_STORE).item(0);
        }

        return movieStore.getElementsByTagName("Genre");
    }

    private void initializeValidation(String mapFilename, DocumentBuilderFactory dbf) {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema s = null;

        try {
            s = sf.newSchema(new File(mapFilename));
        } catch (SAXException e) {
            e.printStackTrace();
        }

        dbf.setValidating(false);
        dbf.setSchema(s);
    }

    private Document createDocument(String filename, DocumentBuilderFactory dbf) throws IOException, SAXException {
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        db.setErrorHandler(new ParseErrorHandler());
        Document document = null;
        document = db.parse(new File(filename));

        return document;
    }

    private void initializeRoot() {
        root = doc.getDocumentElement();
    }
}
