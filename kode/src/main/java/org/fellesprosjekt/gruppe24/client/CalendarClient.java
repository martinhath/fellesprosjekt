package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import org.fellesprosjekt.gruppe24.common.KryoUtils;

import java.io.IOException;

public class CalendarClient {

    Client client;
    int portTCP;
    int portUDP;
    String host;

    public CalendarClient(String host, int portTCP, int portUDP){
        client = new com.esotericsoftware.kryonet.Client();
        this.host = host;
        this.portTCP = portTCP;
        this.portUDP = portUDP;
    }

    public void init(){
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
}
