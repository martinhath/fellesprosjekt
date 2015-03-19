package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.*;
import org.fellesprosjekt.gruppe24.common.models.net.*;
import org.fellesprosjekt.gruppe24.database.GroupDatabaseHandler;
import org.fellesprosjekt.gruppe24.database.GroupNotificationHandler;
import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;
import org.fellesprosjekt.gruppe24.database.MeetingNotificationHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NotificationController extends ServerController {
    Logger logger = Logger.getLogger(getClass().getName());

    public NotificationController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public Response post(Request req) {
        // TODO: implement
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Response put(Request req) {
        MeetingNotificationHandler mnhandler = MeetingNotificationHandler.GetInstance();
        GroupNotificationHandler gnhandler = GroupNotificationHandler.GetInstance();
        NotificationRequest r = (NotificationRequest) req;
        Notification notification = (Notification) req.payload;
        if (notification == null) {
            return Response.GetFailResponse("Payload was null.");
        }
        boolean success = false;
        if (notification instanceof MeetingNotification) {
            MeetingNotification meetingNotification = (MeetingNotification) req.payload;
            success = mnhandler.update(meetingNotification);
        } else if (notification instanceof GroupNotification) {
            GroupNotification groupNotification = (GroupNotification) req.payload;
            success = gnhandler.update(groupNotification);
        }
        Response res = new Response();
        if (success) {
            res.type = Response.Type.OK;
        } else {
            res.type = Response.Type.FAIL;
        }
        return res;
    }

    // trenger man noen gang å hente en notification?
    @Override
    public Response get(Request req) {
        // TODO: implement
        throw new RuntimeException("Not implemented");
    }

    /**
     * Send either User or Meeting in the payload to get its notifications.
     * <p/>
     * Returns either all the notifications, all the group notifications or all the meeting notifications
     * of the User in the payload. Which type of notifications depends on the Handler enum in the request.
     * <p/>
     * Returns all the meeting notifications of the Meeting in the payload.
     *
     * @param req Requetsen fra klienten
     */
    @Override
    public Response list(Request req) {
        NotificationRequest nr = (NotificationRequest) req;
        if(nr.payload == null) {
        	return Response.GetFailResponse("Payload was null!");
        }
        Response res;
        if(req.payload instanceof User) {
        	res = getAllNotifications(nr, (User) nr.payload);
        } else if(req.payload instanceof Meeting) {
        	res = getAllNotifications(nr, (Meeting) nr.payload);
        } else {
        	res = Response.GetFailResponse("Payload was invalid!");
        }
        return res;
    }
    
    private Response getAllNotifications(NotificationRequest nr, User user) {
        List<Notification> result = new ArrayList<Notification>();
        List<MeetingNotification> meetingNotifications = new ArrayList<MeetingNotification>();
        List<GroupNotification> groupNotifications = new ArrayList<GroupNotification>();

        MeetingNotificationHandler mnhandler = MeetingNotificationHandler.GetInstance();
        GroupDatabaseHandler ghandler = GroupDatabaseHandler.GetInstance();

        try {
            meetingNotifications = mnhandler.getAllOfUser(user.getId());
            groupNotifications = ghandler.getAllGroupInvites(user);
        } catch (NullPointerException ex) {
            return Response.GetFailResponse("User did not have ID");
        }
        
        for (Notification notification : meetingNotifications) {
        	if (notification != null) {
        		if(nr.includeRead || (!nr.includeRead && !notification.isRead()))
        			result.add(notification);
        	}
        }
        for (Notification notification : groupNotifications) {
        	if (notification != null) {
        		if(nr.includeRead || (!nr.includeRead && !notification.isRead()))
        			result.add(notification);
        	}
        }

        switch (nr.handler) {
            case MEETING:
                result.removeAll(groupNotifications);
            case GROUP:
                result.removeAll(meetingNotifications);
        }

        Response res = new Response(Response.Type.OK, result);
        return res;
    }
    
    private Response getAllNotifications(NotificationRequest nr, Meeting meeting) {
    	if(meeting == null) {
    		return Response.GetFailResponse("Payload was null");
    	}
    	MeetingNotificationHandler mnHandler = MeetingNotificationHandler.GetInstance();
    	List<MeetingNotification> result = mnHandler.getAllOfMeeting(meeting.getId());
    	Response res = new Response(Response.Type.OK, result);
        return res;
    }

	@Override
	public Response delete(Request req) {
        throw new RuntimeException("Not implemented!");
	}
}
