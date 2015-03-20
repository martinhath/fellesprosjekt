package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.models.GroupNotification;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.GroupDatabaseHandler;
import org.fellesprosjekt.gruppe24.database.GroupNotificationHandler;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class GroupController extends ServerController{
    Logger logger = Logger.getLogger(getClass().getName());

    public GroupController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public Response post(Request req) {
        if (!(req.payload instanceof Group)) {
            return Response.GetFailResponse("Wrong payload: not Group");
        }
        Group g = (Group) req.payload;
        GroupDatabaseHandler handler = GroupDatabaseHandler.GetInstance();
        GroupNotificationHandler gnhandler = GroupNotificationHandler.GetInstance();
        UserDatabaseHandler uhandler = UserDatabaseHandler.GetInstance();

        List<User> members = g.getMembers();
        User owner = uhandler.get(g.getOwnerId());

        g = handler.insert(g);

        // inviterer medlemmer til gruppa
        String invitationString;
        for (User member : members) {
            invitationString = String.format("Du er invitert til %s av %s", g, owner);
            handler.addUserToGroup(member, g, invitationString);
        }

        Response res = new Response();
        if(g == null) {
            res.type = Response.Type.FAIL;
            res.payload = "Something went wrong";
        } else {
            res.type = Response.Type.OK;
            res.payload = g;
        }
        return res;
    }

    @Override
    public Response put(Request req) {
    	if (!(req.payload instanceof Group)) {
            return Response.GetFailResponse("Wrong payload: not Group");
        }
    	Group g = (Group) req.payload;
    	GroupDatabaseHandler handler = GroupDatabaseHandler.GetInstance();
        GroupNotificationHandler gnhandler = GroupNotificationHandler.GetInstance();
        UserDatabaseHandler uhandler = UserDatabaseHandler.GetInstance();
    	handler.insert(g);

        User owner = uhandler.get(g.getOwnerId());

        List<User> deletedMembers = handler.getAllUsersInGroup(g);
        List<User> currentMembers = g.getMembers();
        currentMembers.removeAll(deletedMembers);
        deletedMembers.removeAll(g.getMembers());


        // sletter brukere som ikke lenger er med
        for (User user : deletedMembers) {
            handler.removeUserFromGroup(user.getId(), g.getId());
        }
        // legger til nye brukere
        for (User user : currentMembers) {
            handler.addUserToGroup(user, g, String.format("Du er invitert til %s av %s", g, owner));
        }

    	g = handler.getGroupFromName(g.getName());

    	Response res = new Response();
    	if(resGroup == null) {
    		res.type = Response.Type.FAIL;
    		res.payload = "Something went wrong";
    		return res;
    	}
    	for(User u : g.getMembers()) {
    		handler.addUserToGroup(u, resGroup, "");
    	}
    	resGroup.setMembers(g.getMembers());
		res.type = Response.Type.OK;
		res.payload = resGroup;
		return res;
    }

    @Override
    public Response get(Request req) {
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
        return res;
    }

    @Override
    public Response list(Request req) {
    	GroupDatabaseHandler handler = GroupDatabaseHandler.GetInstance();
    	Object pl = req.payload;
    	List<Group> groups = null;
    	if(pl instanceof User) {
    		groups = handler.getAllGroupsForUser((User) pl);
    	} else {
    		groups = handler.getAll();
    	}
        Response res = new Response();
        if(groups == null) {
            return Response.GetFailResponse("Fant ingen grupper for '"
                    + ((Entity) req.payload).getName() + "'");
        }
        else {
        	for(Group g : groups) {
        		g.setMembers(handler.getAllUsersInGroup(g));
        	}
        	res.type = Response.Type.OK;
        }
        res.payload = groups;
        return res;
    }

	@Override
	public Response delete(Request req) {
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
        return res;
	}
}
