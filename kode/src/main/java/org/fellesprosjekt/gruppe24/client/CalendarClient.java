package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import org.fellesprosjekt.gruppe24.common.KryoUtils;

import java.io.IOException;

public class CalendarClient {

    private static CalendarClient calendarClient;

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
