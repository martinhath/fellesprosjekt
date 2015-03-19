package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.MeetingRequest;
import org.fellesprosjekt.gruppe24.common.models.net.NotificationRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.listeners.MeetingListener;
import org.fellesprosjekt.gruppe24.server.listeners.NotificationListener;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeetingController extends ServerController {
    Logger logger = Logger.getLogger(getClass().getName());

    public MeetingController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public Response post(Request req) {
        Meeting meeting = (Meeting) req.payload;
        if (meeting == null) {
            return Response.GetFailResponse("payload was null");
        }

        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();
        Meeting resMeeting = handler.insert(meeting);
        Set<User> uniques = new HashSet<User>();
        for (Entity participant : meeting.getParticipants()) {
            if (participant instanceof Group) {
            	Group group = (Group) participant;
            	for (Entity e : group.getMembers()) {
            		uniques.add((User) e);
            	}
            } else {
                uniques.add((User) participant);
            }
        }
        for(User u : uniques) {
        	String s;
        	if(resMeeting.getOwner().equals(u))
        		s = "Du opprettet møtet '" + resMeeting.getName() + "'";
        	else
        		s = String.format("Du er invitert til '%s' av %s", resMeeting.getName(), meeting.getOwner().getName());
        	handler.addUserToMeeting(
                    resMeeting,
                    u,
                    s);
        }
        /* Sender notifications på nytt til brukerene som har fått
         * ny notifikasjon
         */
        broadcast(new LinkedList<>(uniques),
                new NotificationController(connection)
                        .list(new NotificationRequest(Request.Type.LIST, false,
                                        NotificationRequest.Handler.BOTH, resMeeting)
                        ));

        broadcast(new LinkedList<>(uniques),
                new MeetingController(connection)
                        .list(new MeetingRequest(Request.Type.LIST, null)
                        ), true);


        Request r = new MeetingRequest(Request.Type.LIST, null);
        simulateReceived(r, MeetingListener.GetInstance(), true);

        Response res = new Response();
        res.type = Response.Type.OK;
        return res;
    }

    @Override
    public Response put(Request req) {
        Meeting meeting = (Meeting) req.payload;
        if (meeting == null) {
            return Response.GetFailResponse("payload was null");
        }

        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();
        handler.update(meeting);
        // finner de som var med i møtet, men kanskje skal fjernes
        List<User> toBeRemoved = handler.getUsersOfMeeting(meeting);
        toBeRemoved.removeAll(meeting.getParticipants());
        List<User> users = new ArrayList<>();
        users.addAll(toBeRemoved);
        // legger til evt nye deltakere
        for (Entity participant : meeting.getParticipants()) {
            if (participant instanceof User) {
                users.add((User) participant);
                handler.addUserToMeeting(
                        meeting,
                        (User) participant,
                        String.format("Du er invitert til %s av %s",
                                meeting, meeting.getOwner()));
            }
        }
        // fjerner de som ikke lenger er med fra databasen
        for (User participant : toBeRemoved) {
            handler.removeUserFromMeeting(meeting, participant);
        }
        Request r = new NotificationRequest(Request.Type.LIST, null);
        simulateReceived(r, NotificationListener.GetInstance(), true);

        r = new MeetingRequest(Request.Type.LIST, null);
        simulateReceived(r, MeetingListener.GetInstance(), true);

        // lager respons
        Response res = new Response();
        res.type = Response.Type.OK;
        return res;
    }

    @Override
    public Response get(Request req) {
        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();
        int id = 0;
        try {
            id = (int) req.payload;
        } catch (ClassCastException ex) {
            logger.log(Level.INFO, ex.getMessage(), ex);
            return Response.GetFailResponse(
                    String.format("bad payload, expected Integer, got %s", req.payload));
        }

        Meeting meeting = handler.get(id);
        if (meeting == null) {
            return Response.GetFailResponse(
                    String.format("could not find meeting with id %d", id));
        }
        Response res = new Response();
        res.type = Response.Type.OK;
        res.payload = meeting;
        return res;
    }

    @Override
    public Response list(Request req) {
        MeetingDatabaseHandler handler = MeetingDatabaseHandler.GetInstance();

        Response res = new Response();
        res.type = Response.Type.OK;
        if (req.payload instanceof User) {
            res.payload = handler.getAll((User) req.payload);
        } else if (req.payload instanceof Group) {
            res.payload = handler.getAll((Group) req.payload);
        } else {
            res.payload = handler.getAll();
        }
        return res;
    }

	@Override
	public Response delete(Request req) {
		// TODO Auto-generated method stub
		MeetingDatabaseHandler mhandler = MeetingDatabaseHandler.GetInstance();
        Meeting m = (Meeting) req.payload;
        if (!mhandler.delete(m)){
            return Response.GetFailResponse(
                    "Could not delete meeting " + m.getName());
        }
        Request r = new MeetingRequest(Request.Type.LIST, null);
        simulateReceived(r, MeetingListener.GetInstance(), true);

        return new Response(Response.Type.OK, null);
	}
}
