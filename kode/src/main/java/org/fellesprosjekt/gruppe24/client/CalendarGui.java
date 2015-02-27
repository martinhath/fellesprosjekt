package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fellesprosjekt.gruppe24.common.KryoUtils;

import java.io.IOException;
import java.net.URL;

public class CalendarGui extends Application{
    CalendarClient calendarClient;

    @Override
    public void start(Stage primaryStage) throws Exception {
        calendarClient = new CalendarClient("127.0.0.1", 9001, 9002);
        Client c = calendarClient.getClient();
        KryoUtils.registerClasses(c.getKryo());
        calendarClient.init();
        try {
            calendarClient.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        URL u = getClass().getResource("/layout/Login.fxml");
        Parent root = FXMLLoader.load(u);
        primaryStage.setTitle("Dette er en tittel");
        Scene s = new Scene(root, 800, 800);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
