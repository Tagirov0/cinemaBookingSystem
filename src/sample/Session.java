package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Session {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty hallId;
    private SimpleIntegerProperty movieId;
    private SimpleStringProperty date;
    private SimpleStringProperty time;
    private SimpleIntegerProperty price;

    public Session(int hallId, int movieId, String date, String time, int price) {
        this.id = new SimpleIntegerProperty();
        this.hallId = new SimpleIntegerProperty(hallId);
        this.movieId = new SimpleIntegerProperty(movieId);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.price = new SimpleIntegerProperty(price);
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public int getPrice() {
        return price.get();
    }
    public void setPrice(int price) {
        this.price.set(price);
    }

    public int getHallId() {
        return hallId.get();
    }
    public void setHallId(int hallId) {
        this.hallId.set(hallId);
    }

    public int getMovieId() {
        return movieId.get();
    }
    public void setMovieId(int movieId) {
        this.movieId.set(movieId);
    }

    public String getDate() {
        return date.get();
    }
    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }
    public void setTime(String time) {
        this.time.set(time);
    }
}
