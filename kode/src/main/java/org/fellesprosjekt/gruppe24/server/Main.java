package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import org.fellesprosjekt.gruppe24.common.KryoUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello, Server!");

        Server server = new Server() {
            protected Connection newConnection(){
                return new ServerConnection();
            }
        };
        server.start();

        KryoUtils.registerClasses(server.getKryo());

        try {
            System.out.println("Starting server on TCP port 9001 and UDP port 9002");
            server.bind(9001, 9002);
        } catch (IOException e) {
            e.printStackTrace();
            server.stop();
            return;
        }

        ServerListener serverListener = new ServerListener(server, null);

        server.addListener(serverListener);
    }
}
