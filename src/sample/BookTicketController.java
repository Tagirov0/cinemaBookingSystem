package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BookTicketController implements Initializable {

    @FXML
    private Button buttonBooking;

    @FXML
    private VBox ticketsVbox;

    @FXML
    private Label movieSelectLabel;

    @FXML
    private Label ticketInfoLabel;

    @FXML
    private Label ticketsNumberLabel;

    String movieTitle;

    private String style = "-fx-font-size: 22pt; -fx-font-weight: bold";

    private ObservableList<String> reserveSeat;
    private ObservableList<ToggleButton> reserveButtons;
    private String date;
    private String time;
    private int hallId;
    private int sessionNumber = 0;
    private int price = 0;

    public void initialize(URL location, ResourceBundle resources) {

        reserveSeat = Data.getInstance().getReserveSeat;
        date = Data.getInstance().selectedDate;
        time = Data.getInstance().selectedTime;
        hallId = Data.getInstance().selectedHallId;

        labelInit();
        buttonBooking.setOnAction(this::booking);
    }

    public void labelInit(){
        for (int i = 0; i < reserveSeat.size(); i++) {
            movieSelectLabel.setText(Data.getInstance().movieTitle);
            movieSelectLabel.setStyle(style);
            ticketInfoLabel.setText(date + ", " + time + ", зал " + hallId);
            ticketInfoLabel.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold; -fx-text-fill: #696969");

            if (reserveSeat.size() == 1)
                ticketsNumberLabel.setText(reserveSeat.size() + " билет");
            else if (reserveSeat.size() > 1 && reserveSeat.size() < 5)
                ticketsNumberLabel.setText(reserveSeat.size() + " билета");
            else
                ticketsNumberLabel.setText(reserveSeat.size() + " билетов");
            ticketsNumberLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold;");

            int seatRow = Data.getInstance().getSeatsInfo("row_num", Integer.parseInt(reserveSeat.get(i)));
            int seatNum = Data.getInstance().getSeatsInfo("seat_number", Integer.parseInt(reserveSeat.get(i)));
            price = Data.getInstance().ticketPrice;
            Label seatInfoLabel = new Label(seatRow + " ряд, " + seatNum + " место \t\t" + price / reserveSeat.size() + " ₽");
            seatInfoLabel.setStyle("-fx-font-size: 12pt");

            ticketsVbox.getChildren().add(seatInfoLabel);
        }
        Label payInfoLabel = new Label("К оплате \t\t       " + price + " ₽");
        payInfoLabel.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold;");
        ticketsVbox.getChildren().add(payInfoLabel);
    }

    private void booking(ActionEvent actionEvent) {
        SeatsController.getInstance().booking(reserveSeat, reserveButtons, sessionNumber, Data.getInstance().getCustomerId());
        Stage stage = (Stage) buttonBooking.getScene().getWindow();
        stage.close();
    }

    public void init(ObservableList<String> reserveSeats, ObservableList<ToggleButton> buttons, int sesNumber, String title){
        buttonBooking.setText("Купить за " + price + " ₽");
        buttonBooking.setStyle("-fx-background-color: black; -fx-font-size: 11pt; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 3");
        reserveSeat = reserveSeats;
        reserveButtons = buttons;
        sessionNumber = sesNumber;
        movieTitle = title;
    }

}
