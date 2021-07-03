package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController implements Initializable {

    @FXML
    TextField firstNameField;

    @FXML
    Label firstNameLabel;

    @FXML
    TextField lastNameField;

    @FXML
    Label lastNameLabel;

    @FXML
    TextField emailField;

    @FXML
    Label emailLabel;

    @FXML
    TextField phoneNumberField;

    @FXML
    Label phoneNumberLabel;

    @FXML
    PasswordField passwordField;

    @FXML
    Label passwordLabel;

    @FXML
    PasswordField confirmPasswordField;

    @FXML
    Label confirmPasswordLabel;

    @FXML
    Label registerMessage;

    @FXML
    Button registrationButton;

    @FXML
    Button closeButton;

    private Pattern pattern;
    private Matcher matcher;
    private boolean checkValidLine = true;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registrationButton.setStyle("-fx-background-color: black; -fx-font-size: 12pt; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 3");
        closeButton.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-font-size: 12pt; -fx-text-fill: black; " +
                "-fx-font-weight: bold; -fx-background-radius: 3");
    }

    public boolean validate(final String hex, String p){
        pattern = Pattern.compile(p);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    private void checkEnteredLine(Label label, TextField textField, String pattern, String errorMessage){
        if (validate(textField.getText(), pattern)){
            label.setText("");
        }
        else {
            label.setText(errorMessage);
            checkValidLine = false;
        }
    }

    public void registrationButtonAction(ActionEvent event) throws IOException {
        checkValidLine = true;

        checkEnteredLine(emailLabel, emailField, EMAIL_PATTERN, "Неверный формат");
        checkEnteredLine(phoneNumberLabel, phoneNumberField, "\\d{11}", "Неверный формат");
        checkEnteredLine(firstNameLabel, firstNameField, "[а-яёА-ЯЁ]{1,30}", "Неверный формат");
        checkEnteredLine(lastNameLabel, lastNameField, "[а-яёА-ЯЁ]{1,30}", "Неверный формат");
        checkEnteredLine(passwordLabel, passwordField, "[a-zA-Z0-9]{6,30}", "Пароль должен содержать от 6 до 30 символов");

        if (passwordField.getText().equals(confirmPasswordField.getText())) {
            confirmPasswordLabel.setText("");
        }
        else {
            confirmPasswordLabel.setText("Пароли не совпадают");
            checkValidLine = false;
        }

        if (checkValidLine) {
            if (registrationUser()){
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
            }
        }
    }

    public boolean registrationUser(){
        boolean check = Data.getInstance().insertCustomers(firstNameField.getText(), lastNameField.getText(),
                emailField.getText(), Long.parseLong(phoneNumberField.getText()), passwordField.getText());

        if (check) {
            registerMessage.setText("Регистрация прошла успешно");
            registerMessage.setStyle("-fx-font-size: 12pt; -fx-text-fill: blue");
            return true;
        }
        else {
            registerMessage.setText("Пользователь с таким Email уже существует");
            registerMessage.setStyle("-fx-font-size: 12pt; -fx-text-fill: red");
            return false;
        }
    }

    public void closeButtonOnAction(ActionEvent e){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
