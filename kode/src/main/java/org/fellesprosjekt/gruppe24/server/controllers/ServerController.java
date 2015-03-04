package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

public abstract class ServerController {

    protected ServerConnection connection;

    public ServerController(ServerConnection conn){
        connection = conn;
    }

    public void setConnection(ServerConnection c){
        connection = c;
    }

    public abstract void post(Request req);

    public abstract void put(Request req);

    public abstract void get(Request req);

    public abstract void list(Request req);

}
