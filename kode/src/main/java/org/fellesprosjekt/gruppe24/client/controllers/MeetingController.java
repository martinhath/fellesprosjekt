package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.Regexes;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.MeetingRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;

import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeetingController extends ClientController {
    private Logger logger = Logger.getLogger(getClass().getName());

    private Meeting meeting;

    @FXML
    private TextField fieldRoom;
    @FXML
    private TextField fieldFromTime;
    @FXML
    private TextField fieldToTime;
    @FXML
    private ComboBox dropdownParticipants;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button buttonOk;
    @FXML
    private Button buttonAbort;

    private LocalTime fromtime;
    private LocalTime totime;

    private List<User> users;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        init();

        UserRequest req = new UserRequest(Request.Type.LIST, null);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener(){
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL)
                    logger.info((String) res.payload);

                if (!(res.payload instanceof List))
                    return;

                try{
                    users = (List<User>) res.payload;
                } catch (ClassCastException e){
                    logger.warning("Payload was of wrong type: " + res.payload);
                }
                populateUsersBox();
                getClient().removeListener(this);
            }
        });
    }

    private void populateUsersBox() {
        for (User u : users) {
            dropdownParticipants.getItems().add(u);
        }
    }

    private void setOKText(Node n) {
        n.setStyle("-fx-text-fill: #333333;");
    }

    private void setErrText(Node n) {
        n.setStyle("-fx-text-fill: #ff4444;");
    }

    public void init() {
        /*
        Gjør det slik at validateASD() funksjonene kjøres
        hver gang fokus for tekstboksene (ol.) endres.
         */
        fieldRoom.focusedProperty().addListener(
                (FocusChangeListener) -> {
                    if (validateRoom())
                        setOKText(fieldRoom);
                    else
                        setErrText(fieldRoom);
                });
        fieldFromTime.focusedProperty().addListener(
                (FocusChangeListener) -> {
                    if (validateFromTime())
                        setOKText(fieldFromTime);
                    else
                        setErrText(fieldFromTime);
                });
        fieldToTime.focusedProperty().addListener(
                (FocusChangeListener) -> {
                    if (validateToTime())
                        setOKText(fieldToTime);
                    else
                        setErrText(fieldToTime);
                });

        meeting = new Meeting();
    }


    /**
     * == VALIDERING ==
     * Dersom et felt ikke er obligatorisk (er det noen)
     * så må validate returnere `true` hvis det er tomt.
     */

    /**
     * Håndterer validaring av tekstfeltet for Room.
     * @return `true` om tekstfeltet er gyldig.
     */
    private boolean validateRoom() {
        // TODO: room regex
        return true;
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
        String string = fieldFromTime.getText().trim();
        Matcher matcher = Regexes.Time.matcher(string);
        if (!matcher.matches()){
            // Vis feilmelding
            return false;
        }
        fromtime = LocalTime.parse(string);
        return true;
    }

    private boolean validateToTime() {
        String string = fieldToTime.getText().trim();
        Matcher matcher = Regexes.Time.matcher(string);
        if (!matcher.matches()){
            // Vis feilmelding
            return false;
        }
        totime = LocalTime.parse(string);
        return true;
    }

    private boolean validateParticipants() {
        // TODO: denne
        return true;
    }

    private boolean validateFields() {
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

    public void clickOk(ActionEvent actionEvent) {
        if (!validateFields())
            return;

        Request req = new MeetingRequest(Request.Type.POST, meeting);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL){
                    logger.info((String) res.payload);
                    // vis noe på skjermen om at det skjedde en feil
                    return;
                }
                getApplication().removeStage(getStage());
                getClient().removeListener(this);
            }
        });
    }

    public void clickAbort(ActionEvent actionEvent) {
        // TODO: Kanskje legge inn en 'er du sikker?' hvis vi har noe data
        getApplication().removeStage(getStage());
    }
}
