package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.server.listeners.InvitationListener;
import org.fellesprosjekt.gruppe24.server.listeners.AuthListener;

import java.io.IOException;

public class CalendarServer {

    private Server server;

    private int portTCP;
    private int portUDP;

    public CalendarServer(int portTCP, int portUDP){
        this.portTCP = portTCP;
        this.portUDP = portUDP;

        server = new Server() {
            protected Connection newConnection(){
                return new ServerConnection();
            }
        };
    }

    public void init(){
        server.addListener(new AuthListener());
        server.addListener(new InvitationListener());
    }

    public void start() throws IOException {
        server.start();
        server.bind(portTCP, portUDP);
        System.out.println(String.format("Starting server on TCP port %d and UDP port %d",
            portTCP, portUDP));
    }


    public void stop(){
        server.stop();
    }

    public Server getServer(){
        return server;
    }


    public static void main(String[] args){
        CalendarServer server = new CalendarServer(9001, 9002);
        KryoUtils.registerClasses(server.getServer().getKryo());
        server.init();

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
