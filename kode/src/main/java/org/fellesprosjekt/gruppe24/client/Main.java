package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import org.fellesprosjekt.gruppe24.common.models.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.User;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Client!");
        Client client = new Client();
        client.start();

        KryoUtils.registerClasses(client.getKryo());

        try{
            client.connect(5000, "127.0.0.1", 9001, 9002);
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
        User user = new User("Martin Thoresen");
        client.sendTCP(user);
    }

}
