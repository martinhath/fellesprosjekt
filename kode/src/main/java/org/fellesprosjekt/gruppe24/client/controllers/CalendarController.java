package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

import javafx.scene.layout.GridPane;
import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.client.components.MeetingPane;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Notification;
import org.fellesprosjekt.gruppe24.common.models.net.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class CalendarController extends ClientController {

    private Logger logger = Logger.getLogger(getClass().getName());

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

    @FXML private Button buttonNotification;
    
    @FXML private GridPane calendarGrid;
    @FXML private ScrollPane scrollPane;

    private LocalDateTime date;

    private List<Meeting> meetings;
    private List<Notification> notifications;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        date = LocalDateTime.now();
        setCalendarLabels(date);

        // 08:00 er øverst
        double kl8 = (scrollPane.getVmax() - scrollPane.getVmin()) * 8 / 13;
        scrollPane.setVvalue(kl8);

        showNotificationCount();
    }

    @Override
    public void init() {
        textBruker.setText("Logget inn som " + getApplication().getUser().getUsername());

        // Får tak i alle møter
        Request req = new MeetingRequest(Request.Type.LIST, getApplication().getUser());
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) return;
                if (!listInstanceOf(res.payload, Meeting.class)) return;

                meetings = (List<Meeting>) res.payload;
                Platform.runLater(CalendarController.this::showMeetings);
                getClient().removeListener(this);
                getClient().removeListener(this);
            }

        });

        req = new NotificationRequest(Request.Type.LIST, getApplication().getUser());
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) return;
                if (!listInstanceOf(res.payload, Notification.class)) return;

                notifications = (List<Notification>) res.payload;
                Platform.runLater(CalendarController.this::showNotificationCount);
                getClient().removeListener(this);
            }
        });
    }

    private void showNotificationCount() {
        if (notifications == null) {
            buttonNotification.setText("Ingen nye meldinger");
            return;
        }
        if (notifications.isEmpty())
            buttonNotification.setText("Ingen nye meldinger");
        else
            buttonNotification.setText(String.format("Nye meldinger (%s)", notifications.size()));
    }

    /**
     * Sørger for at alle møter for denne uken vises i kalenderen.
     */
    private void showMeetings() {
        // Fjerner møtene som vises nå.
        List<Node> toRemove = new LinkedList<>();
        calendarGrid.getChildren().forEach((node) -> {
            if (node instanceof MeetingPane)
                toRemove.add(node);
        });
        for (Node node:toRemove)
            calendarGrid.getChildren().remove(node);
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

        MeetingPane pane = new MeetingPane(this, m);
        calendarGrid.add(pane, col, row);
        int duration = m.getTo().getHour() - m.getFrom().getHour() + 1;
        GridPane.setRowSpan(pane, duration);
    }

    /**
     * Setter alle lablene til uken som begynner med `date` datoen.
     * @param date Datoen som uken begynner på.
     */
    private void setCalendarLabels(LocalDateTime date) {
        int i = date.getDayOfWeek().getValue();
        date = date.minusDays(i-1);
        labelMon.setText(date.plusDays(0).format(Formatters.dayformat));
        labelTue.setText(date.plusDays(1).format(Formatters.dayformat));
        labelWed.setText(date.plusDays(2).format(Formatters.dayformat));
        labelThu.setText(date.plusDays(3).format(Formatters.dayformat));
        labelFri.setText(date.plusDays(4).format(Formatters.dayformat));
        labelSat.setText(date.plusDays(5).format(Formatters.dayformat));
        labelSun.setText(date.plusDays(6).format(Formatters.dayformat));

        labelWeek.setText("Uke " + date.format(Formatters.weekformat));
        labelMonth.setText(date.format(Formatters.monthformat));
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
        showMeetings();
    }

    @FXML
    public void seeNotifications(ActionEvent a) {
    	String path = Layout.Notification;
    	getApplication().newScene(path);
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
