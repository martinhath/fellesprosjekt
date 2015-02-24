package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.User;

import java.io.IOException;
import java.util.List;

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

        List<Group> grupper = user.getGroups();
        grupper.add(new Group("halla"));

        System.out.println(user.getGroups().size());

        client.sendTCP(user);

        client.addListener(new Listener() {
            public void received(Connection conn, Object obj) {
                System.out.println(obj.toString());
            }
        });
    }

}
