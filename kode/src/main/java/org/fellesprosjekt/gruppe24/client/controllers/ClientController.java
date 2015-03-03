package org.fellesprosjekt.gruppe24.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

import org.fellesprosjekt.gruppe24.client.CalendarApplication;
import org.fellesprosjekt.gruppe24.client.CalendarClient;

import com.esotericsoftware.kryonet.Client;

public abstract class ClientController implements Initializable{
    private CalendarApplication application;
    private Client client;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
        client = CalendarClient.GetInstance().getClient();
	}
    
    public Client getClient(){
    	return client;
    }

	public void setApplication(CalendarApplication application){
        this.application = application;
    }

    public CalendarApplication getApplication(){
        return this.application;
    }
}
