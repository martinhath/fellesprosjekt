package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.*;
import org.fellesprosjekt.gruppe24.common.models.net.*;
import org.fellesprosjekt.gruppe24.database.GroupNotificationHandler;
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
        switch (r.handler) {
            case MEETING:
                MeetingNotification meetingNotification = (MeetingNotification) req.payload;
                success = mnhandler.update(meetingNotification);
            case GROUP:
                GroupNotification groupNotification = (GroupNotification) req.payload;
                success = gnhandler.update(groupNotification);
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
        NotificationRequest r = (NotificationRequest) req;
        if (req.payload == null) {
            Response res = Response.GetFailResponse("Payload was null");
            connection.sendTCP(res);
        }
        List<Notification> result = new ArrayList<Notification>();
        List<MeetingNotification> meetingNotifications = new ArrayList<MeetingNotification>();
        List<GroupNotification> groupNotifications = new ArrayList<GroupNotification>();

        MeetingNotificationHandler mnhandler = MeetingNotificationHandler.GetInstance();
        GroupNotificationHandler gnhandler = GroupNotificationHandler.GetInstance();

        if (req.payload instanceof User) { // User in payload
            User user = (User) req.payload;
            try {
                meetingNotifications = mnhandler.getAllOfUser(user.getId());
                groupNotifications = gnhandler.getAllOfUser(user.getId());
            } catch (NullPointerException ex) {
                Response res = Response.GetFailResponse("User did not have ID");
                connection.sendTCP(res);
            }
            if (r.includeRead)
                result.addAll(meetingNotifications);
            else {
                for (Notification notification : meetingNotifications) {

                    if (notification != null && notification.isRead())
                        result.remove(notification);
                }
            }
            if (r.includeRead) result.addAll(groupNotifications);
            else {
                for (Notification notification : groupNotifications) {
                    if (notification != null && notification.isRead())
                        result.remove(notification);
                }
            }

            switch (r.handler) {
                case MEETING:
                    result.removeAll(groupNotifications);
                case GROUP:
                    result.removeAll(meetingNotifications);
            }
        } else if (req.payload instanceof Meeting) { // Meeting in payload
            Meeting meeting = (Meeting) req.payload;
            result.addAll(mnhandler.getAllOfMeeting(meeting.getId()));
        } else { // Nothing in payload
            Response res = Response.GetFailResponse("Payload was not User or Meeting.");
        }

        Response res = new Response(Response.Type.OK, result);
        connection.sendTCP(res);
    }

	@Override
	public void delete(Request req) {
        throw new RuntimeException("Not implemented!");
	}
}
