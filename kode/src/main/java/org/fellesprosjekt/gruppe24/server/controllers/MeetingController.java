package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.List;
import java.util.logging.Level;
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

        System.out.println("Nytt møte:");
        System.out.println(meeting);

        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();
        Meeting resMeeting = handler.insert(meeting);
        for (Entity participant : meeting.getParticipants()) {
            if (participant.getClass() == Group.class) {
                // TODO add group to meeting

            } else {
                handler.addUserToMeeting(
                        meeting,
                        (User) participant,
                        String.format("Du er invitert til %s av %s", meeting, meeting.getOwner()));
            }
        }
        Response res = new Response();
        res.type = Response.Type.OK;
        res.payload = resMeeting;
        connection.sendTCP(res);
    }

    @Override
    public void put(Request req) {
        Meeting meeting = (Meeting) req.payload;
        if (meeting == null) {
            connection.sendTCP(Response.GetFailResponse("payload was null"));
            return;
        }

        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();
        handler.update(meeting);
        // finner de som var med i møtet, men kanskje skal fjernes
        List<User> toBeRemoved = handler.getUsersOfMeeting(meeting);
        toBeRemoved.removeAll(meeting.getParticipants()); // remove all er differanseoperatoren i mengdelære
        // legger til evt nye deltakere
        for (Entity participant : meeting.getParticipants()) {
            if (participant.getClass() == Group.class) {
                // TODO add group to meeting

            } else {
                handler.addUserToMeeting(
                        meeting,
                        (User) participant,
                        String.format("Du er invitert til %s av %s", meeting, meeting.getOwner()));
            }
        }
        // fjerner de som ikke lenger er med fra databasen
        for (User participant : toBeRemoved) {
            handler.removeUserFromMeeting(meeting, participant);
        }
        // lager respons
        Response res = new Response();
        res.type = Response.Type.OK;
        connection.sendTCP(res);
    }

    @Override
    public void get(Request req) {
        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();
        int id = 0;
        try {
            id = (int) req.payload;
        } catch (ClassCastException ex) {
            logger.log(Level.INFO, ex.getMessage(), ex);
            connection.sendTCP(Response.GetFailResponse(
                    String.format("bad payload, expected Integer, got %s", req.payload)));
            return;
        }

        Meeting meeting = handler.get(id);
        if (meeting == null) {
            connection.sendTCP(Response.GetFailResponse(String.format("could not find meeting with id %d", id)));
            return;
        }
        Response res = new Response();
        res.type = Response.Type.OK;
        res.payload = meeting;
        connection.sendTCP(res);
    }

    @Override
    public void list(Request req) {
        // TODO støtte for å ta med møtene til en bruker eller gruppe
        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();
        Response res = new Response();
        res.type = Response.Type.OK;
        res.payload = handler.getAll();
        connection.sendTCP(res);
    }

	@Override
	public void delete(Request req) {
		// TODO Auto-generated method stub
		
	}
}
