package org.fellesprosjekt.gruppe24.server;

public abstract class ServerController {

    ServerConnection connection;


    public ServerController(ServerConnection conn){
        connection = conn;
    }

    public void setConnection(ServerConnection c){
        connection = c;
    }

}
