package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeetingDatabaseHandler extends DatabaseHandler<Meeting> {
    private static Logger lgr = Logger.getLogger(MeetingDatabaseHandler.class.getName());

    private static MeetingDatabaseHandler instance;

    /**
     * Singleton-pattern.
     * Les på wikipedia, eller se `CalendarClient.java`
     *
     * @return Objektet.
     */
    public static MeetingDatabaseHandler GetInstance() {
        if (instance == null) return new MeetingDatabaseHandler();
        else return instance;
    }

    public ArrayList<Meeting> getAll() {
        ArrayList<Meeting> allMeetings = new ArrayList<Meeting>();
        String query = "SELECT * FROM Meeting;";
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
        for (HashMap<String, String> hm : result) {
            allMeetings.add(generateMeeting(hm));
        }
        return allMeetings;
    }

    /**
     * Inserts a meeting in the database based on a meeting object
     *
     * @param meeting
     */
    public Meeting insert(Meeting meeting) {
        try {
            int roomId;
            if (meeting.getRoom() == null) roomId = 0;
            else roomId = meeting.getRoom().getId();
            int groupId;
            if (meeting.getGroup() == null) groupId = 0;
            else groupId = meeting.getGroup().getId();
            return insert(
                    meeting.getName(),
                    meeting.getDescription(),
                    meeting.getFrom(),
                    meeting.getTo(),
                    roomId,
                    meeting.getOwner().getId(),
                    groupId,
                    meeting.getLocation());
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    private Meeting insert(String name,
                           String description,
                           LocalDateTime from,
                           LocalDateTime to,
                           int roomId,
                           int ownerId,
                           int groupId,
                           String location) throws SQLException {
        String query =
                "INSERT INTO Meeting " +
                        "(name, description, start_time, end_time, Room_roomid, owner_id, Group_groupid, location) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
        ps.setString(1, name);
        ps.setString(2, description);
        ps.setTimestamp(3, java.sql.Timestamp.valueOf(from));
        ps.setTimestamp(4, java.sql.Timestamp.valueOf(to));
        if (roomId == 0) ps.setNull(5, roomId);
        else ps.setInt(5, roomId);
        ps.setInt(6, ownerId);
        if (groupId == 0) ps.setNull(7, groupId);
        else ps.setInt(7, groupId);
        ps.setString(8, location);

        int newId = DatabaseManager.executePS(ps);
        return new Meeting(
                newId,
                name,
                description,
                RoomDatabaseHandler.GetInstance().get(roomId),
                from,
                to,
                location,
                new ArrayList<Entity>(),
                UserDatabaseHandler.GetInstance().get(ownerId));
    }

    public Meeting generateMeeting(HashMap<String, String> info) {
        try {
            Room room = info.get("Room_roomid") == null ?
                    null : RoomDatabaseHandler.GetInstance().get(Integer.parseInt(info.get("Room_roomid")));
            Meeting meeting = new Meeting(
                    Integer.parseInt(info.get("meetingid")),
                    info.get("name"),
                    info.get("description"),
                    room,
                    DatabaseManager.stringToDateTime(info.get("start_time")),
                    DatabaseManager.stringToDateTime(info.get("end_time")),
                    info.get("location"),
                    new ArrayList<Entity>(),
                    UserDatabaseHandler.GetInstance().get(Integer.parseInt(info.get("owner_id")))
            );
            return meeting;
        } catch (Exception ex) {
            return null;
        }
    }

    public Meeting get(int id) {
        try {
            HashMap<String, String> info = DatabaseManager.getRow(String.format("SELECT * FROM Meeting WHERE meetingid=%d", id));
            return generateMeeting(info);
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }


    public boolean delete(Meeting meeting) {
        try {
            DatabaseManager.updateQuery(String.format("DELETE FROM Meeting WHERE meetingid=%d", meeting.getId()));
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    public boolean update(Meeting meeting) {
        // TODO
        return true;
    }

    public void addUserToMeeting(Meeting meeting, User user, String message) {
        try {
            insertUserInvitedToMeeting(meeting.getId(), user.getId(), message);
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    private void insertUserInvitedToMeeting(int meetingid, int userid, String message) throws SQLException {
        String query = "INSERT INTO User_invited_to_meeting " +
                "(User_userid, Meeting_meetingid, notification_message) " +
                "VALUES (?, ?, ?)";
        PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
        ps.setInt(1, userid);
        ps.setInt(2, meetingid);
        ps.setString(3, message);
        DatabaseManager.executePS(ps);
    }

    public List<User> getUsersOfMeeting(Meeting meeting) {
        List<User> result = new ArrayList<>();
        try {
            for (int userid : selectUserIDsOfMeeting(meeting.getId())) {
                result.add(UserDatabaseHandler.GetInstance().get(userid));
            }
            return result;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return result;
        }
    }

    private List<Integer> selectUserIDsOfMeeting(int meetingid) throws SQLException {
        List<Integer> result = new ArrayList<>();
        String query = String.format("SELECT User_userid FROM User_invited_to_meeting WHERE Meeting_meetingid=%d", meetingid);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row: resultSet) {
            result.add(Integer.parseInt(row.get("User_userid")));
        }
        return result;
    }

    public void removeUserFromMeeting(Meeting meeting, User user) {
        try {
            deleteUserInvitedToMeeting(meeting.getId(), user.getId());
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void deleteUserInvitedToMeeting(int meetingid, int userid) throws SQLException {
        DatabaseManager.deleteRow("User_invited_to_meeting", "meeting", "user", meetingid, userid);
    }

}
