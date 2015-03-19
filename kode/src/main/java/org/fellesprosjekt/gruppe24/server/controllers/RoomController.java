package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.RoomRequest;
import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;
import org.fellesprosjekt.gruppe24.database.RoomDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RoomController extends ServerController {
    Logger logger = Logger.getLogger(getClass().getName());

    public RoomController(ServerConnection conn) {
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
        // Meeting meeting = (Meeting) req.payload;
        // if (meeting == null) Response.GetFailResponse("Meeting was null.");
        // RoomDatabaseHandler rhandler = RoomDatabaseHandler.GetInstance();
        //
        // List<Room> rooms = rhandler.getAvailableRooms(meeting);
        List<Room> rooms = RoomDatabaseHandler.GetInstance().getAll();

        Response res = new Response(Response.Type.OK, rooms);
        connection.sendTCP(res);

    }

	@Override
	public void delete(Request req) {
        throw new RuntimeException("Not implemented!");
	}
}
