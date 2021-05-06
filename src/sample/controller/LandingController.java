package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.utilities.Client;

import java.io.IOException;

public class LandingController {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordTextField;

    public void loginBtnHandler(ActionEvent event) {
        if (Client.accountExist(usernameTextField.getText(), passwordTextField.getText())) {

        } else {
            errorLabel.setText("wrong credentials");
            errorLabel.setVisible(true);
        }

        errorLabel.setVisible(false);
        createClientStage();
    }

    public void createClientStage() {
        try {
            Client.username = usernameTextField.getText();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/client.fxml"));
            Stage clientStage = new Stage();
            clientStage.setScene(new Scene(root, 800, 500));
            clientStage.show();
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
