package com.sma11new.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DisclaimerController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private static boolean confirmed = false;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == confirmButton) {
            confirmed = true;
        } else if (event.getSource() == cancelButton) {
            confirmed = false;
        }

        // 获取 Stage 并关闭
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public static boolean isConfirmed() {
        return confirmed;
    }
}
