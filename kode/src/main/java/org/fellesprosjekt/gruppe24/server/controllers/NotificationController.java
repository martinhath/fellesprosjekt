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
    public void post(Request req) {
        // TODO: implement
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void put(Request req) {
        MeetingNotificationHandler mnhandler = MeetingNotificationHandler.GetInstance();
        GroupNotificationHandler gnhandler = GroupNotificationHandler.GetInstance();
        NotificationRequest r = (NotificationRequest) req;
        Notification notification = (Notification) req.payload;
        if (notification == null) {
            connection.sendTCP(Response.GetFailResponse("Payload was null."));
        }
        boolean success = false;
        if (notification instanceof MeetingNotification) {
            MeetingNotification meetingNotification = (MeetingNotification) req.payload;
            success = mnhandler.update(meetingNotification);
        } else if (notification instanceof GroupNotification) {
            GroupNotification groupNotification = (GroupNotification) req.payload;
            success = gnhandler.update(groupNotification);
        } else {
            logger.warning("HVA VIL DU !??!? ");
        }
        Response res = new Response();
        if (success) {
            res.type = Response.Type.OK;
        } else {
            res.type = Response.Type.FAIL;
        }
        connection.sendTCP(res);
    }

    // trenger man noen gang å hente en notification?
    @Override
    public void get(Request req) {
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
    public void list(Request req) {
        NotificationRequest nr = (NotificationRequest) req;
        if(nr.payload == null) {
        	Response res = Response.GetFailResponse("Payload was null!");
            connection.sendTCP(res);
            return;
        }
        if(req.payload instanceof User) {
        	getAllNotifications(nr, (User) nr.payload);
        } else if(req.payload instanceof Meeting) {
        	getAllNotifications(nr, (Meeting) nr.payload);
        } else {
        	Response res = Response.GetFailResponse("Payload was invalid!");
            connection.sendTCP(res);
        }
    }
    
    private void getAllNotifications(NotificationRequest nr, User user) {
        System.out.println(nr);
        System.out.println(nr.includeRead);
        List<Notification> result = new ArrayList<Notification>();
        List<MeetingNotification> meetingNotifications = new ArrayList<MeetingNotification>();
        List<GroupNotification> groupNotifications = new ArrayList<GroupNotification>();

        MeetingNotificationHandler mnhandler = MeetingNotificationHandler.GetInstance();
        GroupDatabaseHandler ghandler = GroupDatabaseHandler.GetInstance();

        try {
            meetingNotifications = mnhandler.getAllOfUser(user.getId());
            groupNotifications = ghandler.getAllGroupInvites(user);
        } catch (NullPointerException ex) {
            Response res = Response.GetFailResponse("User did not have ID");
            connection.sendTCP(res);
        }
        
        for (Notification notification : meetingNotifications) {
        	if (notification != null) {
                for(Notification not: meetingNotifications) {
                    System.out.println(not.getMessage());
                    System.out.println(not.isRead());
                }
        		if(nr.includeRead || (!nr.includeRead && !notification.isRead()))
        			result.add(notification);
        	}
        }
        for (Notification notification : groupNotifications) {
        	if (notification != null) {
                for(Notification not: groupNotifications) {
                    System.out.println(not.getMessage());
                    System.out.println(not.isRead());
                }
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
        connection.sendTCP(res);
    }
    
    private void getAllNotifications(NotificationRequest nr, Meeting meeting) {
    	if(meeting == null) {
    		Response res = Response.GetFailResponse("Payload was null");
    		connection.sendTCP(res);
    		return;
    	}
    	MeetingNotificationHandler mnHandler = MeetingNotificationHandler.GetInstance();
    	List<MeetingNotification> result = mnHandler.getAllOfMeeting(meeting.getId());
    	Response res = new Response(Response.Type.OK, result);
        connection.sendTCP(res);
    }

	@Override
	public void delete(Request req) {
        throw new RuntimeException("Not implemented!");
	}
}
