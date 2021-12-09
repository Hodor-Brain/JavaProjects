package com.company.Module2.Lab2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieStoreDAO {
    private final Connection connection;

    public MovieStoreDAO() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        final String user = "postgres";
        final String password = "14062002";
        final String url = "jdbc:postgresql://localhost:5432/postgres@localhost";

        this.connection = DriverManager.getConnection(url, user, password);
    }

    public MovieStoreDAO(String DBName, String ip, int port) throws Exception {
        Class.forName("org.postgresql.Driver");

        final String user = "guest";
        final String password = "123123";
        final String url = String.format("jdbc:postgresql://%s:%d/%s", ip, port, DBName);

        connection = DriverManager.getConnection(url, user, password);
    }

    public List<Genre> getGenres() {
        PreparedStatement statement = null;
        List<Genre> genres = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT id, name FROM Genres");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                genres.add(new Genre(result.getInt("id"), result.getString("name")));
            }
            result.close();
        } catch (SQLException e) {
            System.out.println("Error during getting genres");
            System.out.println(" >> " + e.getMessage());
        } finally {
            return genres;
        }
    }

    public void showGenres() {
        System.out.println("Genres:");

        for (Genre genre : getGenres()) {
            System.out.println(genre);
        }
    }

    public boolean addGenre(Genre genre) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO  Genres (name) VALUES (?)");
            statement.setString(1, genre.getName());

            statement.executeUpdate();
            System.out.printf("Genre %s successfully added!%n", genre.toString());
            return true;
        } catch (SQLException e) {
            System.out.printf("Genre %s was not added!%n", genre.toString());
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public boolean updateGenre(String oldGenre, Genre newGenre) throws Exception {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("Update Genres Set name=? Where name=?");

            statement.setString(1, newGenre.getName());
            statement.setString(2, oldGenre);

            statement.executeUpdate();
            System.out.printf("Genre %s was successfully updated!%n", oldGenre);
            return true;
        } catch (SQLException e) {
            System.out.printf("Genre %s was not updated!%n", oldGenre);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public boolean deleteGenre(String name) {
        PreparedStatement deleteGenreStatement = null, deleteFilmsStatement = null;
        try {
            deleteGenreStatement = connection.prepareStatement("DELETE FROM Genres WHERE Genres.name = ?");
            deleteGenreStatement.setString(1, name);

            deleteFilmsStatement = connection.prepareStatement("DELETE FROM Films WHERE Films.genreId = " +
                    "(SELECT Genres.id FROM Genres WHERE Genres.name = ?)");
            deleteFilmsStatement.setString(1, name);

            deleteFilmsStatement.executeUpdate();
            int result = deleteGenreStatement.executeUpdate();

            if (result > 0) {
                System.out.printf("Genre %s was successfully deleted%n", name);
                return true;
            } else {
                System.out.printf("There is no genre with name %s%n", name);
                return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error during deleting genre with name %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public List<Film> getFilms() {
        PreparedStatement statement = null;
        List<Film> films = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT Genres.id AS genreId, Genres.name AS genreName,  Films.id" +
                    " AS filmId,  Films.name AS filmName, Films.duration AS filmDuration\n" +
                    "FROM Genres INNER JOIN Films on Genres.id = Films.genreid");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                films.add(new Film(result.getInt("filmId"),
                                result.getString("filmName"),
                                result.getFloat("filmDuration"),
                                new Genre(result.getInt("genreId"),
                                        result.getString("genreName")
                                )
                        )
                );
            }
            result.close();

        } catch (SQLException e) {
            System.out.println("Error during getting films");
            System.out.println(" >> " + e.getMessage());
        } finally {
            return films;
        }
    }

    public void showFilms() {
        System.out.println("Films:");

        for (Film film : getFilms()) {
            System.out.println(film);
        }
    }


    public boolean addFilm(Film film) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO films (name, duration, genreId) VALUES (?, ?, ?)");
            statement.setString(1, film.getName());
            statement.setFloat(2, film.getDuration());
            statement.setInt(3, getGenreId(film.getGenre().getName()));

            statement.executeUpdate();
            System.out.printf("Film %s successfully added!%n", film.toString());
            return true;
        } catch (SQLException e) {
            System.out.printf("Film %s was not added!%n", film.toString());
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public boolean updateFilm(Film oldFilm, Film newFilm) throws Exception {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("Update Films Set name=?, duration=?, genreId=? Where name=?");

            statement.setString(1, newFilm.getName());
            statement.setFloat(2, newFilm.getDuration());
            statement.setInt(3, newFilm.getGenre().getId());
            statement.setString(4, oldFilm.getName());

            statement.executeUpdate();
            System.out.printf("Film %s was successfully updated!%n", oldFilm.toString());
            return true;
        } catch (SQLException e) {
            System.out.printf("Film %s was not updated!%n", oldFilm.toString());
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public boolean deleteFilm(String name) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM Films WHERE Films.name = ?");

            statement.setString(1, name);

            int result = statement.executeUpdate();

            if (result > 0) {
                System.out.printf("Film %s was successfully deleted%n", name);
                return true;
            } else {
                System.out.printf("There is no film with name %s%n", name);
                return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error during deleting film with name %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public int getNumberOfFilmsByGenre(String genreName) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT COUNT(*) AS result \n" +
                    "FROM Films INNER JOIN Genres ON Films.genreId = Genres.id\n" +
                    "WHERE Genres.name = ?");

            statement.setString(1, genreName);

            ResultSet queryResult = statement.executeQuery();
            queryResult.next();
            int result = queryResult.getInt("result");

            queryResult.close();

            return result;
        } catch (SQLException e) {
            System.out.printf("Error during getting films with genre %s%n", genreName);
            System.out.println(" >> " + e.getMessage());
            return -1;
        }
    }


    public Film getFilmByName(String filmName) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT Films.id AS filmId, Films.genreId AS genreId, Films.name " +
                    "AS filmName, Films.duration AS duration FROM Films WHERE Films.name=?");
            statement.setString(1, filmName);

            ResultSet queryResult = statement.executeQuery();
            queryResult.next();
            Film film = new Film(queryResult.getInt("filmId"), queryResult.getString("filmName"),
                    queryResult.getFloat("duration"), getGenreById(queryResult.getInt("genreId")));

            queryResult.close();

            return film;


        } catch (SQLException e) {
            System.out.printf("Error during getting film by name %s%n", filmName);
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }


    public List<Film> getFilmsByGenre(String genreName) {
        PreparedStatement statement = null;
        List<Film> films = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT Genres.id AS genreId, Genres.name AS genreName,  Films.id" +
                    " AS filmId,  Films.name AS filmName, Films.duration AS filmDuration\n" +
                    "FROM Genres INNER JOIN Films on Genres.id = Films.genreid " +
                    "WHERE Genres.name = ?");

            statement.setString(1, genreName);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                films.add(new Film(result.getInt("filmId"),
                                result.getString("filmName"),
                                result.getFloat("filmDuration"),
                                new Genre(result.getInt("genreId"),
                                        result.getString("genreName")
                                )
                        )
                );
            }
            result.close();
            return films;
        } catch (SQLException e) {
            System.out.printf("Error during getting films by genre %s%n", genreName);
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }


    public int getGenreId(String name) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement("SELECT id FROM Genres WHERE Genres.name = ?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            result.next();
            int id = result.getInt("id");
            result.close();
            return id;
        } catch (SQLException e) {
            System.out.printf("Error during getting genre id for %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return -1;
        }
    }


    public Genre getGenreById(int id) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement("SELECT name FROM Genres WHERE Genres.id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            String genreName = result.getString("name");
            result.close();
            return new Genre(id, genreName);
        } catch (SQLException e) {
            System.out.println("Error during getting genre");
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }


    public void stop() throws SQLException {
        connection.close();
    }
}
