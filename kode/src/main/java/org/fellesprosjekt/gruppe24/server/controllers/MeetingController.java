package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.logging.Logger;

public class MeetingController extends ServerController {
    Logger logger = Logger.getLogger(getClass().getName());

    public MeetingController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public void post(Request req) {
        Meeting meeting = (Meeting) req.payload;
        if (meeting == null) {
            connection.sendTCP(Response.GetFailResponse("payload was null"));
            return;
        }

        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();
        handler.insert(meeting);
        // Håndtere invitasjoner mm.
        Response res = new Response();
        res.type = Response.Type.OK;
        connection.sendTCP(res);
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
