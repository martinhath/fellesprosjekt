package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.listeners.ServerListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class ServerController {

    protected static List<ServerConnection> connections = new LinkedList<>();

    protected ServerConnection connection;

    public ServerController(ServerConnection conn){
        connection = conn;
        if (conn != null) {
            if (connections.indexOf(conn) == -1)
                connections.add(conn);
        }
    }

    public void setConnection(ServerConnection c){
        connection = c;
        if (connections.indexOf(c) == -1)
            connections.add(c);
    }

    /**
     * Sender en response til alle tilkoblede klienter
     *
     * @param users Brukere som skal motta responsen.
     * @param res Responsen som skal sendes.
     * @param userAsPayload Om brukeren til hver connection skal være payload.
     */
    public void broadcast(List<User> users, Response res, boolean userAsPayload) {
        for (Iterator<ServerConnection> it = connections.iterator();
             it.hasNext();) {
            ServerConnection con = it.next();
            if (!users.contains(con.getUser()))
                // skal ikke sendes til
                continue;
            if (!con.isConnected())
                // er borte
                it.remove();
            else {
                if (userAsPayload) res.payload = con.getUser();
                con.sendTCP(res);
            }
        }
    }

    public void broadcast(List<User> users, Response res) {
        broadcast(users, res, false);
    }

    public void broadcast(Response res){
        broadcast(new LinkedList<>(), res);
    }

    /**
     * Vi later som denne requesten ble sendt fra en klient, og
     * den skal nå sendes til alle.
     * @param req
     */
    public void simulateReceived(Request req, ServerListener listener, boolean useUser) {
        for (ServerConnection c : connections ) {
            if (useUser) {
                req.payload = c.getUser();
            }
            listener.received(c, req);
        }
    }

    public void simulateReceived(Request req, ServerListener listener) {
        simulateReceived(req, listener, false);
    }

    /**
     * Brukes når klienten sender info til serveren,
     * uten at det nødvendigvis skal lagres noe sted.
     * Eks, innlogging.
     * @param req Requesten fra klienten.
     */
    public abstract Response post(Request req);

    /**
     * Brukes når klienten sender info til serveren
     * som _skal_ lagres. (kanskje vi ikke trenger denne?)
     * @param req Requesten fra klienten
     */
    public abstract Response put(Request req);

    /**
     * Brukes når vi skal ha et eller flere objekter
     * basert på en parameter.
     * Eks, få brukeren med en Id
     * Eks, få gruppene en bruker er med i
     * @param req Requesten fra klienten
     */
    public abstract Response get(Request req);
    
    /**
     * Brukes når vi skal slette et objekt (en rad)
     * fra databasen.
     * @param req Requesten fra klienten
     */
    public abstract Response delete(Request req);

    /**
     * Brukes når vi skal få alt av noe.
     * Eks, få alle brukere
     * @param req Requetsen fra klienten
     */
    public abstract Response list(Request req);

}
