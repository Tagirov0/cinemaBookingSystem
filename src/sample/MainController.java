package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



// #2F2B43

public class MainController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private AnchorPane anPane;

    @FXML
    private Label genreLabel;

    @FXML
    private ImageView img;

    @FXML
    private Label directorLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label durationLabel;

    @FXML
    public ComboBox<String> movieSelectBox;

    private ObservableList<Movie> movies;
    public int movieNumber;
    private int selectedMovie;

    SeatsController seatsController = new SeatsController();

    private static MainController mainController = new MainController();

    public static MainController getMainController(){
        return mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        movieSelectBox.setValue("Выберите фильм");

        movies = Data.getInstance().listMovies();
        movieNumber = 0;

        // Инициализация выпадающего списка с фильмами
        for (int i = 0; i < movies.size(); i++) {
            movieSelectBox.getItems().addAll(movies.get(i).getTitle());
        }
        movieSelectBox.setValue(movies.get(0).getTitle());
        movieInfo();
        movieSelectBox.setOnAction(this::displayMovieInfo);

    }

    private void displayMovieInfo(ActionEvent actionEvent) {
        movieInfo();
    }

    // Вывод на экран информации о фильме после выбора в combobox
    private void movieInfo(){
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getTitle() == movieSelectBox.getValue()) {
                movieNumber = i;
                break;
            }
        }
        labelInit(movieNumber);


        Data.getInstance().selectedMovieNumber(movieNumber);
        Data.getInstance().selectedMovieId(movies.get(movieNumber).getId());
    }

    // Инициализация всех Label (информации о фильме)
    private void labelInit(int k){
        img.setImage(new Image("file:"+ movies.get(k).getUrl()));
        yearLabel.setText("Год производства      " + movies.get(k).getYear());
        genreLabel.setText("Жанр                           " + movies.get(k).getGenre());
        directorLabel.setText("Режиссер                    " + movies.get(k).getDirector());
        ageLabel.setText("Возраст                       " + movies.get(k).getAgeLimit() + "+");
        durationLabel.setText("Время                          " + movies.get(k).getDuration());
        descriptionLabel.setText(movies.get(k).getDescription());
    }

    // переход на страницу seats при нажатии кнопки "Расписание и билеты"
    public void switchOnSeatsScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("seats.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
    }
}
