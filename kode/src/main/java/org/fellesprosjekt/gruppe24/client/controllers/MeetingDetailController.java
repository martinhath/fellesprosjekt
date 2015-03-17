package org.fellesprosjekt.gruppe24.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.common.models.Meeting;

import java.net.URL;
import java.util.ResourceBundle;

public class MeetingDetailController extends ClientController {

    private Meeting meeting;

    @FXML public Label labelTitle;
    @FXML public TextArea textDesc;
    @FXML public TextField textRoom;
    @FXML public DatePicker datePicker;
    @FXML public TextField textFrom;
    @FXML public TextField textTo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        textDesc.setDisable(true);
        textRoom.setDisable(true);
        datePicker.setDisable(true);
        textFrom.setDisable(true);
        textTo.setDisable(true);
    }

    public void setMeeting(Meeting m) {
        meeting = m;
        setFields();
    }

    private void setFields() {
        if (meeting == null) return;

        labelTitle.setText(meeting.getName());
        textDesc.setText(meeting.getDescription());
        textRoom.setText(meeting.getRoom().getName());
        datePicker.setValue(meeting.getFrom().toLocalDate());
        textFrom.setText(meeting.getFrom().format(Formatters.hhmmformat));
        textTo.setText(meeting.getTo().format(Formatters.hhmmformat));
    }

    public void clickEdit(ActionEvent actionEvent) {
        // hva skal skje her? Åpne nytt vindu, eller gjøre felt redigerbare?
    }

    public void clickBack(ActionEvent actionEvent) {
        getApplication().setScene(getStage(), Layout.Calendar);
    }
}
