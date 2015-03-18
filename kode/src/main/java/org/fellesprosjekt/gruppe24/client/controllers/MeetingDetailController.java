package org.fellesprosjekt.gruppe24.client.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.common.Regexes;
import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;

import java.awt.event.FocusListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Matcher;

public class MeetingDetailController extends ClientController {

    private Meeting meeting;

    private boolean isOwner = false;
    private boolean editMode = false;

    @FXML public Label labelTitle;
    @FXML public TextArea textDesc;
    @FXML public ComboBox<Entity> comboOwner;
    @FXML public ComboBox<Room> comboRoom;
    @FXML public DatePicker datePicker;
    @FXML public TextField textFrom;
    @FXML public TextField textTo;

    @FXML public ListView<Entity> listParticipants;
    @FXML public ListView<Entity> listInvited;

    @FXML public Button buttonEdit;

    private LocalTime totime;
    private LocalTime fromtime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        textFrom.focusedProperty().addListener((observable, notfocus, focus) -> {
            if (focus) return;
            if (validateFromTime())
                setOKText(textFrom);
            else
                setErrText(textFrom);
        });

        textTo.focusedProperty().addListener((observable, notfocus, focus) -> {
            if (focus) return;
            if (validateToTime())
                setOKText(textTo);
            else
                setErrText(textTo);
        });
    }

    private void checkAdmin() {
        if (getApplication().getUser().getId() == meeting.getOwner().getId()){
            isOwner = true;
            buttonEdit.setVisible(true);
        } else {
            buttonEdit.setVisible(false);
        }
    }

    public void setMeeting(Meeting m) {
        meeting = m;
        checkAdmin();
        setFields();
    }

    private void setFields() {
        textDesc.setEditable(editMode);
        comboOwner.setEditable(editMode);
        comboRoom.setEditable(editMode);
        datePicker.setEditable(editMode);
        textFrom.setEditable(editMode);
        textTo.setEditable(editMode);

        if (meeting == null)
            return;

        comboOwner.getItems().add(meeting.getOwner());
        comboOwner.getSelectionModel().select(meeting.getOwner());
        labelTitle.setText(meeting.getName());
        textDesc.setText(meeting.getDescription());
        datePicker.setValue(meeting.getFrom().toLocalDate());
        textFrom.setText(meeting.getFrom().format(Formatters.hhmmformat));
        textTo.setText(meeting.getTo().format(Formatters.hhmmformat));

        for (Entity e: meeting.getParticipants()){
            listParticipants.getItems().add(e);
        }
    }

    private void setOKText(Node n) {
        n.setStyle("-fx-text-fill: #333333;");
    }

    private void setErrText(Node n) {
        n.setStyle("-fx-text-fill: #ff4444;");
    }

    private boolean validateRoom() {
        return comboOwner.getSelectionModel().getSelectedItem() != null;
    }

    private boolean validateDate() {
        // Skal man kunne registrere møter i fortiden?
        if ( datePicker.getValue() == null)
            return false;
        try {
            // antar at et møte bare kan vare i én dag.
            LocalDate date = datePicker.getValue();
            LocalDateTime ldt = LocalDateTime.from(date.atStartOfDay());
            meeting.setFrom(ldt);
            meeting.setTo(ldt);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean validateFromTime() {
        String string = textFrom.getText().trim();
        Matcher matcher = Regexes.Time.matcher(string);
        if (!matcher.matches()){
            // Vis feilmelding
            return false;
        }
        fromtime = LocalTime.parse(string);
        return true;
    }

    private boolean validateToTime() {
        String string = textTo.getText().trim();
        Matcher matcher = Regexes.Time.matcher(string);
        if (!matcher.matches()){
            // Vis feilmelding
            return false;
        }
        totime = LocalTime.parse(string);
        return true;
    }

    private boolean validateParticipants() {
        int n = listInvited.getItems().size() +
                listParticipants.getItems().size();
        return n != 0;
    }

    private boolean validateFields() {
        // TODO: denne.
        // bør vise hvilke felter som er gale.
        boolean b =  validateRoom() && validateDate() &&
                validateFromTime() && validateToTime() &&
                validateParticipants();
        if (!b)
            return false;
        int mins = totime.getHour()*60 + totime.getMinute();
        meeting.getFrom().plusMinutes(mins);

        mins = fromtime.getHour() * 60 + fromtime.getMinute();
        meeting.getTo().plusMinutes(mins);

        return true;
    }

    public void clickEdit(ActionEvent actionEvent) {
        if (editMode){
            if (!validateFields())
                return;
            editMode = false;
            buttonEdit.setText("Rediger");
        } else {
            editMode = true;
            buttonEdit.setText("Ferdig");
        }
        setFields();
    }

    public void clickBack(ActionEvent actionEvent) {
        // Må sende PUT request til server, for å oppdatere møtet.
        getApplication().setScene(getStage(), Layout.Calendar);
    }
}
