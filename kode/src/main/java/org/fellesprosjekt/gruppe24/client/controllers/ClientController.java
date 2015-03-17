package org.fellesprosjekt.gruppe24.client.controllers;

import java.awt.*;
import java.awt.Button;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.fellesprosjekt.gruppe24.client.CalendarApplication;
import org.fellesprosjekt.gruppe24.client.CalendarClient;

import com.esotericsoftware.kryonet.Client;

import javax.swing.*;

public abstract class ClientController implements Initializable {

    private String path;
    private CalendarApplication application;
    private Client client;

    private Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = CalendarClient.GetInstance().getClient();
    }

    public void init() {
    }

    ;

    public Client getClient() {
        return client;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage s) {
        stage = s;
    }


    public void setApplication(CalendarApplication application) {
        this.application = application;
    }

    public CalendarApplication getApplication() {
        return this.application;
    }

}