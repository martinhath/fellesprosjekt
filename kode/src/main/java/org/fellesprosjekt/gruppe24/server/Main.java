package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.fellesprosjekt.gruppe24.common.models.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.User;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello, Server!");
        Server server = new Server();
        server.start();

        KryoUtils.registerClasses(server.getKryo());

        try {
            System.out.println("Starting server on TCP port 9001 and UDP port 9002");
            server.bind(9001, 9002);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.addListener(new Listener() {
            public void received(Connection conn, Object obj) {
                if (obj instanceof User){
                    User user = (User) obj;
                    System.out.println("Vi har fått en bruker");
                    System.out.println("Brukeren heter " + user.getName());
                }
            }
        });
    }
}
