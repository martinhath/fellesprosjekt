package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.MeetingNotification;
import org.fellesprosjekt.gruppe24.common.models.Room;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MeetingNotificationHandler extends DatabaseHandler<MeetingNotification> {
    private String fromTable = "User_invited_to_meeting";
    private static Logger lgr = Logger.getLogger(MeetingDatabaseHandler.class.getName());
    UserDatabaseHandler uhandler = UserDatabaseHandler.GetInstance();
    MeetingDatabaseHandler mhandler = MeetingDatabaseHandler.GetInstance();

    private static MeetingNotificationHandler instance;

    /**
     * Singleton-pattern.
     * Les på wikipedia, eller se `CalendarClient.java`
     *
     * @return Objektet.
     */
    public static MeetingNotificationHandler GetInstance() {
        if (instance == null) return new MeetingNotificationHandler();
        else return instance;
    }

    public MeetingNotification generateMeetingNotification(HashMap<String, String> info) {
        try {
            String time = info.get("alarm_time");
            MeetingNotification notification = new MeetingNotification(
                    uhandler.get(Integer.parseInt(info.get("user_userid"))),
                    info.get("notification_message"),
                    info.get("notification_read").equals("1"),
                    info.get("confirmed").equals("1"),
                    mhandler.get(Integer.parseInt(info.get("meeting_meetingid"))),
                    info.get("hide").equals("1"),
                    time == null || time.equals("") ?
                            null :
                            DatabaseManager.stringToTime(time)
            );
            return notification;
        } catch (Exception ex) {
            lgr.severe(ex.toString());
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Notifications are generated automatically when creating a meeting, so this one is not implemented until it is
     * needed
     *
     * @param notification
     * @return null
     */
    @Override
    public MeetingNotification insert(MeetingNotification notification) {
        return null;
    }

    /**
     * This one cannot be used, and returns only null, since you need both userId and meetingId
     *
     * @param id Id-en til objektet.
     * @return null
     */
    @Override
    public MeetingNotification get(int id) {
        return null;
    }

    public MeetingNotification get(int userId, int meetingId) {
        String query = String.format(
                "SELECT * FROM %s WHERE user_userid = %s AND meeting_meetingid = %s;", fromTable, userId, meetingId);
        try {
            HashMap<String, String> info = DatabaseManager.getRow(String.format(query));
            return generateMeetingNotification(info);
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public List<MeetingNotification> getAll() {
        List<MeetingNotification> result = new ArrayList<MeetingNotification>();
        String query = String.format("SELECT * FROM %s", fromTable);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row : resultSet) {
            result.add(generateMeetingNotification(row));
        }
        return result;
    }

    public List<MeetingNotification> getAllOfUser(int userId) {
        List<MeetingNotification> result = new ArrayList<MeetingNotification>();
        String query = String.format("SELECT * FROM %s WHERE user_userid = %s", fromTable, userId);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row : resultSet) {
            MeetingNotification n = generateMeetingNotification(row);
            if (n != null)
                result.add(n);
        }
        return result;
    }

    public List<MeetingNotification> getAllOfMeeting(int meetingId) {
        List<MeetingNotification> result = new ArrayList<MeetingNotification>();
        String query = String.format("SELECT * FROM %s WHERE meeting_meetingid = %s", fromTable, meetingId);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row : resultSet) {
            MeetingNotification m = generateMeetingNotification(row);
            if (m != null)
                result.add(m);
        }
        return result;
    }

    @Override
    public boolean delete(MeetingNotification notification) {
        return mhandler.removeUserFromMeeting(notification.getMeeting(), notification.getUser());
    }

    @Override
    public boolean update(MeetingNotification notification) {
        String query = "" +
                "UPDATE User_invited_to_meeting SET notification_message = ?, notification_read = ?, confirmed = ?, alarm_time = ? " +
                "WHERE user_userid = ? AND meeting_meetingid = ?;";
        PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
        try {
            LocalTime time = notification.getAlarmTime();
            ps.setString(1, notification.getMessage());
            ps.setBoolean(2, notification.isRead());
            ps.setBoolean(3, notification.isConfirmed());
            ps.setTime(4, time == null ?
                    null :
                    java.sql.Time.valueOf(time));
            ps.setInt(5, notification.getUser().getId());
            ps.setInt(6, notification.getMeeting().getId());
            return DatabaseManager.executePS(ps) != -1;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }
}
