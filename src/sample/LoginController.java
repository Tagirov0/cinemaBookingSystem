package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label loginMessageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setStyle("-fx-background-color: black; -fx-font-size: 11pt; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 3");
        registerButton.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-font-size: 11pt; -fx-text-fill: black; " +
                "-fx-font-weight: bold; -fx-background-radius: 3");
    }

    public void loginButtonOnAction(ActionEvent actionEvent) throws IOException {
        loginMessageLabel.setText("");

        if (validate()) {
            Data.getInstance().customerEmail(emailTextField.getText());
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        }
    }

    public boolean validate(){
        boolean check = Data.getInstance().validateLogin(emailTextField.getText(), passwordField.getText());

        if (emailTextField.getText().isEmpty() == false && passwordField.getText().isEmpty() == false) {
            if (check) {
                loginMessageLabel.setText("");
                return true;
            }
            else {
                loginMessageLabel.setText("Неверный Email или пароль");
                return false;
            }
        }
        else {
            loginMessageLabel.setText("Введите email и пароль");
            return false;
        }
    }

    public void switchOnRegisterScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("registerForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
