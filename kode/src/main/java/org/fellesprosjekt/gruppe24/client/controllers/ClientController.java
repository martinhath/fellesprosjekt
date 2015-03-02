package org.fellesprosjekt.gruppe24.client.controllers;

import javafx.fxml.Initializable;
import org.fellesprosjekt.gruppe24.client.CalendarApplication;

public abstract class ClientController implements Initializable{
    private CalendarApplication application;

    public void setApplication(CalendarApplication application){
        this.application = application;
    }

    public CalendarApplication getApplication(){
        return this.application;
    }
}
