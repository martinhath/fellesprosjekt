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
