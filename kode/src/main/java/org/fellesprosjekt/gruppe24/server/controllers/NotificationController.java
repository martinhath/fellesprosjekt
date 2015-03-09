package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.logging.Logger;

public class NotificationController extends ServerController{
    Logger logger = Logger.getLogger(getClass().getName());

    public NotificationController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public void post(Request req) {
        // TODO: implement
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void put(Request req) {
        // TODO: implement
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void get(Request req) {
        // TODO: implement
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void list(Request req) {
        // TODO: implement
        throw new RuntimeException("Not implemented");
    }
}
