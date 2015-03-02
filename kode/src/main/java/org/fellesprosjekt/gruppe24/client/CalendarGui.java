package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fellesprosjekt.gruppe24.common.KryoUtils;

import java.io.IOException;

public class CalendarGui extends Application{

    CalendarClient calendarClient;
    Stage stage;
    Scene scene;
    Initializable controller;

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

    public Initializable setScene(String path){
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
        return (Initializable) loader.getController();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
