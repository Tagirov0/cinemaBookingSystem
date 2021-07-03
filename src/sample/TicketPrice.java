package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TicketPrice {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty sessionId;
    private SimpleIntegerProperty price;

    public TicketPrice(int sessionId, int price) {
        this.id = new SimpleIntegerProperty();
        this.sessionId = new SimpleIntegerProperty(sessionId);
        this.price = new SimpleIntegerProperty(price);
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public int getSessionId() {
        return sessionId.get();
    }
    public void setSessionId(int sessionId) {
        this.sessionId.set(sessionId);
    }

    public int getPrice() {
        return price.get();
    }
    public void setPrice(int price) {
        this.price.set(price);
    }

}
