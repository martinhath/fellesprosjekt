package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.fellesprosjekt.gruppe24.client.Components.MeetingPane;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.MeetingRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class CalendarController extends ClientController {

    private Logger logger = Logger.getLogger(getClass().getName());

    @FXML private Button meetingButton;
    @FXML private Label labelMon;
    @FXML private Label labelTue;
    @FXML private Label labelWed;
    @FXML private Label labelThu;
    @FXML private Label labelFri;
    @FXML private Label labelSat;
    @FXML private Label labelSun;
    @FXML private Label labelWeek;
    @FXML private Label labelMonth;
    @FXML private Text textBruker;
    
    @FXML private GridPane calendarGrid;

    private DateTimeFormatter dayformat = DateTimeFormatter.ofPattern("EEEE d.");
    private DateTimeFormatter weekformat = DateTimeFormatter.ofPattern("w");
    private DateTimeFormatter monthformat = DateTimeFormatter.ofPattern("MMMM");

    private LocalDateTime date;

    private List<Meeting> meetings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        date = LocalDateTime.now();
        setCalendarLabels(date);

        Request req = new MeetingRequest(Request.Type.LIST, /*getClient().getUser()*/null);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (!(res.payload instanceof List))
                    return;

                try {
                    meetings = (List<Meeting>) res.payload;
                } catch (ClassCastException e) {
                    logger.info("Wrong response: " +  res.payload);
                    return;
                }
                showMeetings();
                getClient().removeListener(this);
            }

        });
    }

    /**
     * Sørger for at alle møter for denne uken vises i kalenderen.
     */
    private void showMeetings() {
        // Fjerner møtene som vises nå.
        calendarGrid.getChildren().forEach((node) -> {
            if (node instanceof MeetingPane)
                calendarGrid.getChildren().remove(node);
        });
        if (meetings == null) return;
        for (Meeting m : meetings) {
            showMeeting(m);
        }
    }

    private void showMeeting(Meeting m) {
        LocalDateTime from = m.getFrom();
        LocalDateTime to = m.getTo();

         // Sjekker om møtet begynner i neste uke, eller sluttet i forige uke.
        LocalDateTime mon = date.minusDays(date.getDayOfWeek().getValue()-1);
        LocalDateTime sun = date.plusDays(7 - date.getDayOfWeek().getValue());
        if (from.isAfter(sun)) return;
        if (to.isBefore(mon)) return;

        int col = from.getDayOfWeek().getValue();
        int row = from.getHour();

        /*
         * TODO:
         * Vi må sjekke om det allerede er møter i kalenderen
         * i samme tidsrom. Hvis det er det kan vi feks gjøre begge
         * møtene halvparten så store i bredden
         */

        MeetingPane pane = new MeetingPane(m);
        pane.setStyle("-fx-background-color: #ff9933;");
        calendarGrid.add(pane, col, row);
        // lel
        GridPane.setColumnSpan(pane, 2);
        GridPane.setRowSpan(pane, 2);
    }

    @Override
    public void init() {
        textBruker.setText("Logget inn som " + getApplication().getUser().getUsername());
    }

    /**
     * Setter alle lablene til uken som begynner med `date` datoen.
     * @param date Datoen som uken begynner på.
     */
    private void setCalendarLabels(LocalDateTime date) {
        int i = date.getDayOfWeek().getValue();
        date = date.minusDays(i-1);
        labelMon.setText(date.plusDays(0).format(dayformat));
        labelTue.setText(date.plusDays(1).format(dayformat));
        labelWed.setText(date.plusDays(2).format(dayformat));
        labelThu.setText(date.plusDays(3).format(dayformat));
        labelFri.setText(date.plusDays(4).format(dayformat));
        labelSat.setText(date.plusDays(5).format(dayformat));
        labelSun.setText(date.plusDays(6).format(dayformat));

        labelWeek.setText("Uke " + date.format(weekformat));
        labelMonth.setText(date.format(monthformat));
    }
    
    private void setLocalWeek(LocalDateTime week){
    	
    }

    @FXML
    public void clickPrevWeek(ActionEvent e) {
        date = date.minusWeeks(1);
        setCalendarLabels(date);
        showMeetings();
    }

    @FXML
    public void clickNextWeek(ActionEvent e) {
        date = date.plusWeeks(1);
        setCalendarLabels(date);
        showMeetings();
    }

    @FXML
    public void newMeeting(ActionEvent a) {
        String path = Layout.NewMeeting;
        getApplication().newScene(path);
    }
    
    @FXML
    public void clickCurrentWeek(ActionEvent e) {
    	date = LocalDateTime.now();
    	setCalendarLabels(date);
    	
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        Request req = new AuthRequest(Request.Type.POST,
                AuthRequest.Action.LOGOUT, null);
        getClient().sendTCP(req);

        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.OK){
                    Platform.runLater(() ->
                        getApplication().setScene(getStage(), Layout.Login)
                    );
                } else {
                    logger.warning((String) res.payload);
                }
                getClient().removeListener(this);
            }
        });
    }
}
