package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.fellesprosjekt.gruppe24.client.controllers.ClientController;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class CalendarApplication extends Application{

    Logger logger = Logger.getLogger(getClass().getName());
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

        controller = setScene("/layout/Login.fxml");

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
