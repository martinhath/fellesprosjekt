package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fellesprosjekt.gruppe24.client.controllers.Controller;
import org.fellesprosjekt.gruppe24.common.KryoUtils;

import java.io.IOException;

public class CalendarGui extends Application{

    CalendarClient calendarClient;
    Stage stage;
    Scene scene;
    Controller controller;

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

        this.stage = primaryStage;

        setScene("/layout/Login.fxml");
    }

    public void setScene(String path){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root;
        try {
            root = loader.load();
            scene = new Scene(root, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
