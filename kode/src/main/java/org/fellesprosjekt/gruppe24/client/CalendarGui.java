package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import javafx.application.Application;
import javafx.stage.Stage;
import org.fellesprosjekt.gruppe24.common.KryoUtils;

import java.io.IOException;

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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
