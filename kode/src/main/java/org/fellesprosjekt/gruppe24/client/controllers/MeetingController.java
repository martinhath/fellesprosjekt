package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;

import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.Regexes;
import org.fellesprosjekt.gruppe24.common.models.*;
import org.fellesprosjekt.gruppe24.common.models.net.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class MeetingController extends ClientController {
    private Logger logger = Logger.getLogger(getClass().getName());

    private Meeting meeting;
    
    //FXML-fält
    @FXML private TextField fieldName;
    @FXML private ChoiceBox<Room> fieldRoom;
    @FXML private TextField fieldFromTime;
    @FXML private TextField fieldToTime;
    @FXML private CheckComboBox<Entity> dropdownParticipants;
    @FXML private DatePicker datePicker;
    @FXML private Button buttonOk;
    @FXML private Button buttonAbort;

    private LocalTime fromtime;
    private LocalTime totime;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        getAllUsers();
        getAllGroups();
        getAllRooms();
    }

    private void getAllUsers() {
        UserRequest req = new UserRequest(Request.Type.LIST, null);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener(){
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) {
                    getClient().removeListener(this);
                    logger.info((String) res.payload);
                    return;
                }

                if (!listInstanceOf(res.payload, User.class)) return;

                List<User> list = (List<User>) res.payload;
                dropdownParticipants.getItems().addAll(list);
                getClient().removeListener(this);
            }
        });
    }

    private void getAllGroups() {
        GroupRequest req = new GroupRequest(Request.Type.LIST, null);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener(){
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) {
                    getClient().removeListener(this);
                    logger.info((String) res.payload);
                    return;
                }

                if (!listInstanceOf(res.payload, Group.class)) return;

                List<Group> list = (List<Group>) res.payload;
                dropdownParticipants.getItems().addAll(list);
                getClient().removeListener(this);
            }
        });
    }

    private void getAllRooms() {
        Request req = new RoomRequest(Request.Type.LIST, null);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener(){
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) {
                    getClient().removeListener(this);
                    logger.info((String) res.payload);
                    return;
                }

                if (!listInstanceOf(res.payload, Room.class)) return;

                List<Room> list = (List<Room>) res.payload;
                fieldRoom.getItems().addAll(list);
                getClient().removeListener(this);
            }
        });
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

    /*
    private boolean validateMeetingName() {
    	String string = fieldName.getText().trim();
    	Matcher matcher = Regexes.Text.matcher(string);
    	if (!matcher.matches()) {
    		//vis felmeddelande
            System.out.println("name fails");
            return false;
    	}
    	return true;
    }
    */
    
    private boolean validateRoom() {
        // Kanskje det er default valg på første rom?
        if (fieldRoom.getSelectionModel().getSelectedItem() == null) {
            return false;
        } 
        return true;
    }

    private boolean validateDate() {
        // Skal man kunne registrere møter i fortiden?
        if ( datePicker.getValue() == null){
            return false;
        } try {
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
        if (dropdownParticipants.getCheckModel().getCheckedItems().size() == 0){
            return false;
        }
        return true;
    }

    private boolean validateFields() {
        boolean b =  /*validateMeetingName() &&*/ validateRoom() &&
                validateDate() && validateFromTime() &&
                validateToTime() && validateParticipants();
        if (!b)
            return false;
        meeting.setName(fieldName.getText());

        meeting.setOwner(getApplication().getUser());

        meeting.setRoom(fieldRoom.getValue());

        int mins = totime.getHour()*60 + totime.getMinute();
        meeting.setFrom(meeting.getFrom().plusMinutes(mins));

        mins = fromtime.getHour() * 60 + fromtime.getMinute();
        meeting.setTo(meeting.getTo().plusMinutes(mins));

        List<Entity> participants = new LinkedList<>();
        for (Entity e : dropdownParticipants.getItems()){
            participants.add(e);
        }
        meeting.setParticipants(participants);
        return true;
    }

    public void clickOk(ActionEvent actionEvent) {
        if (!validateFields())
            return;

        System.out.println(meeting);
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
                Platform.runLater(() -> {
                    getApplication().removeStage(getStage());
                });
                getClient().removeListener(this);
            }
        });
    }

    public void clickAbort(ActionEvent actionEvent) {
        // TODO: Kanskje legge inn en 'er du sikker?' hvis vi har noe data
        getApplication().removeStage(getStage());
    }
}
