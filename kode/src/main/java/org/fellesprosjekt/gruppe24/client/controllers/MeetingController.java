package org.fellesprosjekt.gruppe24.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MeetingController extends ClientController {

    @FXML
    Button buttonOk;

    @FXML
    Button buttonAbort;

    public void clickOk(ActionEvent actionEvent) {
        // TODO: logikk
        getApplication().removeStage(getStage());
    }

    public void clickAbort(ActionEvent actionEvent) {
        // TODO: logikk
        getApplication().removeStage(getStage());
    }
}
