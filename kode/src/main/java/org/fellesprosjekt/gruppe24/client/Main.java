package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Client!");
        Client client = new Client();
        client.start();
        try{
            client.connect(5000, "127.0.0.1", 9001, 9002);
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
        String str = "Jeg er så glad hver julekveld";
        client.sendTCP(str);
    }
}
