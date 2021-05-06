package sample.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.utilities.Client;

public class ClientController{

    @FXML
    private Button sendBtn;

    @FXML
    private TextArea memoTextArea;

    @FXML
    private TextArea chatTextArea;

    @FXML
    private TextField chatTextField;

    @FXML
    public void initialize() {
        Client.start("localhost", 8080, memoTextArea, chatTextArea);
    }

    public void sendBtnHandler(ActionEvent event) {
        Client.sendMessage(chatTextField.getText());
        chatTextField.setText("");
    }
}
