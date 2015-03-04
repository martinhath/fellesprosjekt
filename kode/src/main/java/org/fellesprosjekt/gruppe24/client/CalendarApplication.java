package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.fellesprosjekt.gruppe24.client.controllers.ClientController;
import org.fellesprosjekt.gruppe24.client.controllers.InvitationController;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.MeetingNotification;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarApplication extends Application{

    CalendarClient calendarClient;
    Stage stage;
    Scene scene;
    ClientController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        calendarClient = CalendarClient.GetInstance();
        calendarClient.setConnectionInfo("127.0.0.1", 9001, 9002);
        calendarClient.init();

        Client c = calendarClient.getClient();
        KryoUtils.registerClasses(c.getKryo());
        try {
            calendarClient.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stage = primaryStage;

        controller = setScene("/layout/Invitation.fxml");
        InvitationController con = (InvitationController) controller;
        User user = new User(52, "Herman", "Karlsen");
        String message = "komsikomsi";
        Room room = new Room(512, "P15", 12, true);
        //LocalDate fradate = LocalDate.now();
        //LocalTime fratime = LocalTime.now();
        LocalDateTime fra = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.of(2015, 03, 05, 12, 10);
        List<Entity> list = new ArrayList<>();
        list.add(user);
        Meeting meeting = new Meeting("Möte", "KRISEMÖTE", room, fra, to, "P15", list, user);
        
        MeetingNotification notification = new MeetingNotification(user, "detta är ett krisemöte", meeting);
        con.setNotification(notification);
    }

    public ClientController setScene(String path){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            final Parent root = loader.load();

            /* Siden denne metoden kan bli kalt fra en
             * annen tråd enn UI-tråden, må vi ha denne.
             */
            Platform.runLater(() -> {
                scene = new Scene(root, 800, 600);
                stage.setScene(scene);
                stage.show();
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ClientController controller = loader.getController();
        if (controller == null){
            System.err.println("loader.getController() returned null!");
            return null;
        }
        controller.setApplication(this);
        this.controller = controller;
        return controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
