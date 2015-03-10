package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.logging.Logger;

public class UserController extends ServerController{
    Logger logger = Logger.getLogger(getClass().getName());

    public UserController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public void put(Request req){
        throw new RuntimeException("Ikke implmentert!");
    }

    @Override
    public void get(Request req){
        throw new RuntimeException("Ikke implmentert!");
    }

    @Override
    public void list(Request req) {
        throw new RuntimeException("Ikke implmentert!");
    }

    @Override
    public void post(Request req) {
        throw new RuntimeException("Ikke implmentert!");
    }
}
