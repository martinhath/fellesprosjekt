package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

public class InvitationController extends ServerController {

	public InvitationController(ServerConnection conn) {
		super(conn);
	}

	@Override
	public Response post(Request req) {
		Response res = new Response();
        res.type = Response.Type.OK;
        return res;
	}

	@Override
	public Response put(Request req) {
		// TODO: implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Response get(Request req) {
		// TODO: implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Response list(Request req) {
		// TODO: implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Response delete(Request req) {
        throw new RuntimeException("Not implemented!");
	}
}
