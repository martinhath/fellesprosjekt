package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.server.ServerConnection;

public abstract class ServerController {

    protected ServerConnection connection;

    public ServerController(ServerConnection conn){
        connection = conn;
    }

    public void setConnection(ServerConnection c){
        connection = c;
    }
}
