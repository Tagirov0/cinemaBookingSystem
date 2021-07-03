package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class SeatsController implements Initializable {

    @FXML
    GridPane seatsGridPane;

    @FXML
    VBox rowNumberLeft;

    @FXML
    VBox rowNumberRight;

    @FXML
    DatePicker datePicker;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private ImageView movieSeatsImage;

    @FXML
    public ComboBox<String> timeSelect;

    @FXML
    private ComboBox<String> movieSelect;

    @FXML
    private Label labelPrice;

    @FXML
    private Label labelHall;

    private String selected = "-fx-background-color: lightgreen; -fx-background-radius: 30; " +
            "-fx-text-fill: white; -fx-font-weight: bold";

    private String available = "-fx-background-color:  #00d0ff; -fx-background-radius: 30; " +
            "-fx-text-fill: white; -fx-font-weight: bold";

    private String unavailable = "-fx-background-color:  red; -fx-background-radius: 30; " +
            "-fx-text-fill: white; -fx-font-weight: bold";

    public Stage stageBooking;

    private ObservableList<Movie> movies;
    private ObservableList<Session> sessions;
    private ObservableList<Ticket> tickets;
    private ObservableList<String> reserveSeat;
    private ObservableList<ToggleButton> reserveButtons;
    private int count = 0;
    private int selectedMovieId;
    private int selectedMovieNumber;
    private int sessionNumber;
    private int ticketCount;

    private static SeatsController seatController = new SeatsController();
    public static SeatsController getMainController(){
        return seatController;
    }

    private int seatCapacity = 0;
    public int price = 0;
    private int selectHallButton = 0;
    private int hall = 0;

    private int rowInHall;
    private int seatsInRow;

    private ToggleButton seatNumber;

    private static SeatsController instance = new SeatsController();

    public static SeatsController getInstance(){
        return instance;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movies = Data.getInstance().listMovies();
        sessions = Data.getInstance().listSessions();
//        prices = Data.getInstance().listPrices();
        tickets = Data.getInstance().listTickets();
        reserveSeat = FXCollections.observableArrayList();
        reserveButtons = FXCollections.observableArrayList();

        selectedMovieId = Data.getInstance().selectedMovieId;
        Data.getInstance().selectedMovieTitle(movies.get(selectedMovieNumber).getTitle());
        selectedMovieNumber = Data.getInstance().selectedMovieNumber;

        anchorPane.setVisible(false);

        // Заполняем выпадающий список movieSelect фильмами
        for (int i = 0; i < movies.size(); i++) {
            movieSelect.getItems().addAll(movies.get(i).getTitle());
        }

        movieSelect.setValue(movies.get(selectedMovieNumber).getTitle());
        labelInit(selectedMovieNumber);

        datePicker.setValue(LocalDate.now());
        timeSelectInit();

        movieSelect.setOnAction(this::displaySessions);
        datePicker.setOnAction(this::displayDateSessions);
        timeSelect.setOnAction(this::displayHall);
//        booked.setOnAction(this::bookTicket);

//        datePicker.setDayCellFactory(picker -> new DateCell() {
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                LocalDate today = LocalDate.now();
//
//                setDisable(empty || date.compareTo(today) < 0 );
//            }
//        });
    }


    // Получение номера фильма
    private void displaySessions(ActionEvent actionEvent) {
        labelPrice.setText("");
        for (int i = 0; i < movies.size(); i++){
            if (movies.get(i).getTitle() == movieSelect.getValue()) {
                count = i;
                break;
            }
        }
        Data.getInstance().selectedMovieTitle(movieSelect.getValue());
        selectedMovieId = movies.get(count).getId();
        labelInit(count);
        timeSelectInit();
        seatsGridPane.getChildren().clear();
        anchorPane.setVisible(false);
    }

    private void displayDateSessions(ActionEvent actionEvent){
        labelPrice.setText("");
        timeSelectInit();
        seatsGridPane.getChildren().clear();
        anchorPane.setVisible(false);
    }

    // Инициализация выпадающего списка с временем сеанса, учитывая тукущее время
    private void timeSelectInit(){
        timeSelect.getItems().clear();
        for (int i = 0; i < sessions.size(); i++) {
            if (selectedMovieId == sessions.get(i).getMovieId()) {
                if (datePicker.getValue().toString().equals(sessions.get(i).getDate())) {
                    if (LocalDate.now().equals(datePicker.getValue())) {
                        if (LocalTime.now().isBefore(LocalTime.parse(sessions.get(i).getTime())))
                            timeSelect.getItems().addAll(sessions.get(i).getTime());
                    }
                    else
                        timeSelect.getItems().addAll(sessions.get(i).getTime());
                }
            }
        }
        Data.getInstance().selectedDate(datePicker.getValue().toString());
    }

    // Вывод номера зала и мест на экран
    private void displayHall(ActionEvent actionEvent){
        labelPrice.setText("");
        anchorPane.setVisible(true);
        seatsGridPane.getChildren().clear();

        for (int i = 0; i < sessions.size(); i++){
            if (sessions.get(i).getTime() == timeSelect.getValue()) {
                Data.getInstance().selectedTime(timeSelect.getValue());
                sessionNumber = i + 1;
                break;
            }
        }
        addButton();
        labelHall.setText("Зал " + sessions.get(sessionNumber).getHallId());
        Data.getInstance().selectedHall(sessions.get(sessionNumber).getHallId());
    }


    // Добавление кнопок(мест) в gridPane, в зависимости от зала
    private void addButton() {
        tickets = Data.getInstance().listTickets();
        reserveButtons.clear();
        reserveSeat.clear();
        price = 0;
        ticketCount = 0;

        seatCapacity = Data.getInstance().getHallCapacity(sessions.get(sessionNumber).getHallId());
        int seatInRow = seatCapacity /  Data.getInstance().getRows(sessions.get(sessionNumber).getHallId());

        rowInHall = 0;
        seatsInRow = 0;

        int firstHallCapacity = 1;
        if (sessions.get(sessionNumber).getHallId() == 2)
            firstHallCapacity = Data.getInstance().getHallCapacity(sessions.get(sessionNumber - 1).getHallId()) + 1;

        //получить общее кол-во мест в кинотеатре


        for (int i = 0; i < seatCapacity; i++) {
            ToggleButton[] buttons = new ToggleButton[seatCapacity];

            if (seatsInRow % seatInRow == 0) {
                rowInHall++;
                seatsInRow = 0;
            }
            seatsInRow++;

            buttons[i] = new ToggleButton(String.valueOf(seatsInRow));
            buttons[i].setPrefSize(50, 48);
            buttons[i].setStyle(available);
            buttons[i].setId(String.valueOf(i + firstHallCapacity));

            seatsGridPane.add(buttons[i], seatsInRow, rowInHall);

            for (Ticket ticket : tickets)
                if (sessionNumber == ticket.getSessionId() && buttons[i].getId().equals(String.valueOf(ticket.getSeatId()))) {

                    buttons[i].setStyle(unavailable);
                    buttons[i].setDisable(true);
                }
            int selectButton = i;
            buttons[selectButton].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    seatNumber = buttons[selectButton];

                    if (reserveSeat.size() == 0) {
                        price = 0;
                        ticketCount = 0;
                    }

                    if (seatNumber.isSelected()) {
                        if (reserveSeat.size() == 5){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Информация");
                            alert.setHeaderText(null);
                            alert.setContentText("За один раз можно заказать не более 5 билетов");
                            // отжимаем кнопку, потому что при повторном нажатии alert не вызовется
                            seatNumber.setSelected(false);
                            alert.showAndWait();
                        }
                        else {
                            reserveButtons.add(seatNumber);
                            reserveSeat.add(seatNumber.getId());
                            seatNumber.setStyle(selected);
                            price += sessions.get(sessionNumber).getPrice();
                            ticketCount += 1;
                            labelPrice.setText(ticketCount + " билета " + price + " ₽");
                        }
                    }
                    else {
                        reserveButtons.remove(seatNumber);
                        reserveSeat.remove(seatNumber.getId());
                        seatNumber.setStyle(available);
                        price -= sessions.get(sessionNumber).getPrice();
                        ticketCount -= 1;
                        if (ticketCount != 0)
                            labelPrice.setText(ticketCount + " билета " + price + " ₽");
                        else
                            labelPrice.setText("");
                    }
                    Data.getInstance().getTicketPrice(price);
                }

            });
        }


        rowNumberLeft.getChildren().clear();
        rowNumberRight.getChildren().clear();

        for (int i = 1; i <= rowInHall; i++) {
            Label rowNumber = new Label(String.valueOf(i));
            rowNumber.setStyle("-fx-text-fill: grey; -fx-font-size: 18");
            rowNumber.setPrefSize(50, 48);
            rowNumberLeft.getChildren().add(rowNumber);

            Label rowNumberR = new Label(String.valueOf(i));
            rowNumberR.setStyle("-fx-text-fill: grey; -fx-font-size: 18");
            rowNumberR.setPrefSize(50, 48);
            rowNumberRight.getChildren().add(rowNumberR);
        }

        Data.getInstance().getReserveSeats(reserveSeat);
    }

    public void booking(ObservableList<String> reserveSeat, ObservableList<ToggleButton> reserveButtons, int sessionNumber, int customerId){
        for (int i = 0; i < reserveSeat.size(); i++) {
            Data.getInstance().insertTicketInfo(sessionNumber, customerId, Integer.parseInt(reserveSeat.get(i)));
        }
        tickets = Data.getInstance().listTickets();
        for (int i = 0; i < reserveSeat.size(); i++) {
            if (reserveButtons.get(i).isSelected()) {
                reserveButtons.get(i).setStyle(unavailable);
                reserveButtons.get(i).setDisable(true);
            }
        }
        reserveButtons.clear();
        reserveSeat.clear();
    }

    private void labelInit(int movieNumber){
        movieSeatsImage.setImage(new Image("file:"+ movies.get(movieNumber).getUrl()));
    }

    public void switchOnMovieScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void BookScene(ActionEvent actionEvent) throws IOException {
        if (reserveSeat.size() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Выберите место");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("booking.fxml"));
            Parent root1 = loader.load();

            BookTicketController bookTicketController = loader.getController();
            bookTicketController.init(reserveSeat, reserveButtons, sessionNumber, movieSelect.getValue());

            stageBooking = new Stage();
            stageBooking.initModality(Modality.APPLICATION_MODAL);
            stageBooking.setScene(new Scene(root1));
            stageBooking.setTitle("Приобретение билетов");
            stageBooking.show();
        }
    }
}
