package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello, Server!");
        Server server = new Server();
        server.start();
        try {
            System.out.println("Starting server on TCP port 9001 and UDP port 9002");
            server.bind(9001, 9002);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.addListener(new Listener() {
            public void received(Connection conn, Object obj) {
                System.out.println(obj.toString());
            }
        });
    }
}
