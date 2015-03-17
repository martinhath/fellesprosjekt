package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.GroupDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.List;
import java.util.logging.Logger;

public class GroupController extends ServerController{
    Logger logger = Logger.getLogger(getClass().getName());

    public GroupController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public void post(Request req) {
        // TODO: implement
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void put(Request req) {
    	if (!(req.payload instanceof Group)) {
            connection.sendTCP(new Response(Response.Type.FAIL,
                    "Wrong payload: not Group"));
            return;
        }
    	Group g = (Group) req.payload;
    	if(g == null) {
    		connection.sendTCP(new Response(Response.Type.FAIL,
    				"Payload error: null"));
            return;
    	}
    	GroupDatabaseHandler handler = GroupDatabaseHandler.GetInstance();
    	handler.insert(g);
    	g = handler.getGroupFromName(g.getName());
    	Response res = new Response();
    	if(g == null) {
    		res.type = Response.Type.FAIL;
    		res.payload = "Something went wrong";
    	} else {
    		res.type = Response.Type.OK;
    		res.payload = g;
    	}
    	connection.sendTCP(res);
    }

    @Override
    public void get(Request req) {
    	Response res = new Response();
    	GroupDatabaseHandler handler = GroupDatabaseHandler.GetInstance();
        if (req.payload instanceof Integer) {
            // payload er Id
            Integer id = (Integer) req.payload;
            res.payload = handler.get(id);
            res.type = Response.Type.OK;
        } else if (req.payload instanceof String) {
            // payload er name
            String name = (String) req.payload;
            res.payload = handler.getGroupFromName(name);
            res.type = Response.Type.OK;
        } else {
            res.type = Response.Type.FAIL;
            res.payload = "Unknown payload type.";
        }
        connection.sendTCP(res);
    }

    @Override
    public void list(Request req) {
    	GroupDatabaseHandler handler = GroupDatabaseHandler.GetInstance();
    	Object pl = req.payload;
    	List<Group> groups = null;
    	if(pl instanceof User) {
    		handler.getAllGroupsForUser((User) pl);
    	} else {
    		groups = handler.getAll();
    	}
        Response res = new Response();
        if(groups == null)
        	res.type = Response.Type.OK;
        else
        	res.type = Response.Type.OK;
        res.payload = groups;
        connection.sendTCP(res);
    }

	@Override
	public void delete(Request req) {
		Response res = new Response();
    	GroupDatabaseHandler handler = GroupDatabaseHandler.GetInstance();
        if (req.payload instanceof Integer) {
            // payload er Id
            Integer id = (Integer) req.payload;
            Group group = handler.get(id);
            handler.delete(group);
            res.type = Response.Type.OK;
        } else if (req.payload instanceof Group) {
            Group group = (Group) req.payload;
            handler.delete(group);
            res.type = Response.Type.OK;
        } else {
            res.type = Response.Type.FAIL;
            res.payload = "Unknown payload type.";
        }
	}
}
