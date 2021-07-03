package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Ticket {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty sessionId;
    private SimpleIntegerProperty movieId;
    private SimpleIntegerProperty hallId;
    private SimpleIntegerProperty seatId;
    private SimpleIntegerProperty rowNumber;
    private SimpleIntegerProperty seatNumber;
    private SimpleStringProperty date;
    private SimpleStringProperty time;

    public Ticket(int sessionId, int movieId, int hallId, int seatId, int rowNumber, int seatNumber, String date, String time) {
        this.sessionId = new SimpleIntegerProperty(sessionId);
        this.movieId = new SimpleIntegerProperty(movieId);
        this.hallId = new SimpleIntegerProperty(hallId);
        this.seatId = new SimpleIntegerProperty(seatId);
        this.rowNumber = new SimpleIntegerProperty(rowNumber);
        this.seatNumber = new SimpleIntegerProperty(seatNumber);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public int getSeatId() {
        return seatId.get();
    }
    public void setSeatId(int seatId) {
        this.seatId.set(seatId);
    }



    public int getHallId() {
        return hallId.get();
    }
    public void setHallId(int hallId) {
        this.hallId.set(hallId);
    }

    public int getRowNumber() {
        return rowNumber.get();
    }
    public void setRowNumber(int rowNumber) {
        this.rowNumber.set(rowNumber);
    }

    public int getSeatNumber() {
        return seatNumber.get();
    }
    public void setSeatNumber(int seatNumber) {
        this.seatNumber.set(seatNumber);
    }

    public int getSessionId() {
        return sessionId.get();
    }
    public void setSessionId(int sessionId) {
        this.sessionId.set(sessionId);
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
