package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Movie {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty year;
    private SimpleStringProperty title;
    private SimpleStringProperty director;
    private SimpleStringProperty duration;
    private SimpleStringProperty genre;
    private SimpleIntegerProperty ageLimit;
    private SimpleStringProperty description;
    private SimpleStringProperty url;


    public Movie(String title, int year, String director, String duration, String genre, int ageLimit, String description, String url) {
        this.id = new SimpleIntegerProperty();
        this.year = new SimpleIntegerProperty(year);
        this.title = new SimpleStringProperty(title);
        this.director = new SimpleStringProperty(director);
        this.duration = new SimpleStringProperty(duration);
        this.genre = new SimpleStringProperty(genre);
        this.ageLimit = new SimpleIntegerProperty(ageLimit);
        this.description = new SimpleStringProperty(description);
        this.url = new SimpleStringProperty(url);
    }


    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }
    public String setTitle(String title) {
        return title;
    }

    public String getDuration() {
        return duration.get();
    }
    public void setDuration(String duration) {
        this.duration.set(duration);
    }

    public String getGenre() {
        return genre.get();
    }
    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getDescription() {
        return description.get();
    }
    public void setDescription(String description) {
        this.description.set(description);
    }

    public int getYear() {
        return year.get();
    }
    public void setYear(int year) {
        this.year.set(year);
    }

    public String getDirector() {
        return director.get();
    }
    public void setDirector(String director) {
        this.director.set(director);
    }

    public int getAgeLimit() {
        return ageLimit.get();
    }
    public void setAgeLimit(int ageLimit) {
        this.ageLimit.set(ageLimit);
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }


}

