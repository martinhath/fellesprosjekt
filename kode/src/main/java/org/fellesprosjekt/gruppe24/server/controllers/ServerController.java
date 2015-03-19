package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.LinkedList;
import java.util.List;

public abstract class ServerController {

    protected static List<ServerConnection> connections = new LinkedList<>();

    protected ServerConnection connection;

    public ServerController(ServerConnection conn){
        connection = conn;
        connections.add(conn);
    }

    public void setConnection(ServerConnection c){
        if (connections.indexOf(connection) != -1)
            connections.remove(connection);
        connection = c;
        connections.add(c);
    }

    /**
     * Sender en request til alle tilkoblede klienter
     *
     * @param req Requesten som skal sendes.
     */
    public void broadcast(Request req) {
        for (ServerConnection con : connections) {
            if (!con.isConnected())
                connections.remove(con);
            else
                con.sendTCP(req);
        }
    }

    /**
     * Sender en response til alle tilkoblede klienter
     *
     * @param res Responsen som skal sendes.
     */
    protected void broadcast(Response res) {
        for (ServerConnection con : connections) {
            if (!con.isConnected())
                connections.remove(con);
            else
                con.sendTCP(res);
        }
    }

    /**
     * Brukes når klienten sender info til serveren,
     * uten at det nødvendigvis skal lagres noe sted.
     * Eks, innlogging.
     * @param req Requesten fra klienten.
     */
    public abstract void post(Request req);

    /**
     * Brukes når klienten sender info til serveren
     * som _skal_ lagres. (kanskje vi ikke trenger denne?)
     * @param req Requesten fra klienten
     */
    public abstract void put(Request req);

    /**
     * Brukes når vi skal ha et eller flere objekter
     * basert på en parameter.
     * Eks, få brukeren med en Id
     * Eks, få gruppene en bruker er med i
     * @param req Requesten fra klienten
     */
    public abstract void get(Request req);
    
    /**
     * Brukes når vi skal slette et objekt (en rad)
     * fra databasen.
     * @param req Requesten fra klienten
     */
    public abstract void delete(Request req);

    /**
     * Brukes når vi skal få alt av noe.
     * Eks, få alle brukere
     * @param req Requetsen fra klienten
     */
    public abstract void list(Request req);

}
