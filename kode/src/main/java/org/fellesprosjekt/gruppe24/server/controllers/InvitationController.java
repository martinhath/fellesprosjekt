package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

public class InvitationController extends ServerController {

	public InvitationController(ServerConnection conn) {
		super(conn);
	}

	@Override
	public void post(Request req) {
		System.out.println("bra jobbat");
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
