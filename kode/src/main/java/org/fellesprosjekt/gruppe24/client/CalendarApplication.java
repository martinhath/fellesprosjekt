package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.fellesprosjekt.gruppe24.client.controllers.ClientController;
import org.fellesprosjekt.gruppe24.common.KryoUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class CalendarApplication extends Application{

    private Logger logger = Logger.getLogger(getClass().getName());

    private CalendarClient calendarClient;
    private List<Stage> stages;
    private Stage primaryStage;
    private Scene scene;
    private ClientController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stages = new LinkedList<>();
        this.primaryStage = primaryStage;

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

        String loginpath = Layout.Login;

        controller = setScene(primaryStage, loginpath);
        controller.setApplication(this);
    }

    public Stage newStage() {
        Stage stage = new Stage();
        stages.add(stage);
        return stage;
    }

    public void removeStage(Stage stage) {
        stages.remove(stage);
        stage.close();
        if (stages.size() == 0){
            calendarClient.stop();
            try{
                stop();
            } catch (Exception e){

            }
        }
    }

    /**
     * Denne funksjonen lager ett nytt vindu, med FXMLen som befinner seg
     * på stien `path`. Kontrolleren som funksjonen returnerer er kontrolleren
     * som styrer vinduet. Denne har tilgang på den nye Stagen.
     * @param path Stien til FXMLen
     * @return Kontrolleren som styrer viewet
     */
    public ClientController newScene(String path) {
        Stage stage = newStage();
        return setScene(stage, path);
    }

    /**
     * Funksjonen endrer viewet som vises i stagen man gir. Blir f.eks. brukt
     * når man logger inn.
     * @param stage Stagen som vi skal endre
     * @param path Stien til FXMLen
     * @return Kontrolleren som styrer viewet
     */
    public ClientController setScene(final Stage stage, String path){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            final Parent root = loader.load();

            /* Siden denne metoden kan bli kalt fra en
             * annen tråd enn UI-tråden, må vi ha denne.
             */
            Platform.runLater(() -> {
                scene = new Scene(root);
                if (stage == null){
                    logger.severe("stage was null: " + path);
                    return;
                }
                stage.setScene(scene);
                stage.sizeToScene();
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
        controller.setStage(stage);
        this.controller = controller;
        return controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
