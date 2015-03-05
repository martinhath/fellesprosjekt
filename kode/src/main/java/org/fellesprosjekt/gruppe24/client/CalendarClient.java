package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class CalendarClient {

    private static CalendarClient calendarClient;
    private Logger logger = Logger.getLogger(getClass().getName());

    Client client;
    int portTCP;
    int portUDP;
    String host;

    private CalendarClient(){}

    public void setConnectionInfo(String host, int portTCP, int portUDP){
        this.host = host;
        this.portTCP = portTCP;
        this.portUDP = portUDP;
    }

    public void init(){
        client = new com.esotericsoftware.kryonet.Client();

        client.addListener(new ClientListener());
    }

    public void start() throws IOException {
        client.start();

        client.connect(5000, host, portTCP, portUDP);
    }

    public void stop(){
        client.close();
    }

    public Client getClient(){
        return client;
    }

    public static CalendarClient GetInstance() {
        if (calendarClient == null)
            calendarClient = new CalendarClient();
        return calendarClient;
    }
}
