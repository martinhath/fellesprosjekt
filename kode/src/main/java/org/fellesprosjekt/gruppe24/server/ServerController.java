package org.fellesprosjekt.gruppe24.server;

import org.fellesprosjekt.gruppe24.common.models.net.Request;

public abstract class ServerController {

    ServerConnection connection;


    public ServerController(ServerConnection conn){
        connection = conn;
    }

    public void setConnection(ServerConnection c){
        connection = c;
    }
}
