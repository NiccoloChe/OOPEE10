package com.example.oopee10.model;

public class Movie {
    private String title;
    private Integer year;
    private String genre;
    private String poster;

    public Movie(String title, Integer year, String genre, String poster) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.poster = poster;
        validate();
    }

    // Validate data
    private void validate() {
        // Title validation
        if (title == null || title.trim().isEmpty()) {
            title = "Unknown Movie";
        }

        // Year validation
        if (year == null || year <= 0 || year > 9999) {
            year = null; // Will display as "Unknown Year"
        }

        // Genre validation
        if (genre == null || genre.trim().isEmpty()) {
            genre = "Uncategorized";
        }

        // Poster validation
        if (poster == null || poster.trim().isEmpty()) {
            poster = "default_movie_poster"; // Default poster resource ID
        }
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getPoster() {
        return poster;
    }

    // Format year display
    public String getFormattedYear() {
        return year != null ? String.valueOf(year) : "Unknown Year";
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year=" + getFormattedYear() +
                ", genre='" + genre + '\'' +
                ", poster='" + poster + '\'' +
                '}';
    }
}