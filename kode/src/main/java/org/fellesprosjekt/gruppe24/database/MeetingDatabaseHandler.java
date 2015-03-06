package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            return insert(
                    meeting.getName(),
                    meeting.getDescription(),
                    meeting.getFrom(),
                    meeting.getTo(),
                    meeting.getRoom().getId(),
                    meeting.getOwner().getId(),
                    meeting.getGroup().getId(),
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
        ps.setInt(5, roomId);
        ps.setInt(6, ownerId);
        ps.setInt(7, groupId);
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

    private Meeting generateMeeting(HashMap<String, String> info) {
        try {
            lgr.log(Level.INFO, "Generating meeting object based on: " + info.toString());
            Meeting meeting = new Meeting(
                    Integer.parseInt(info.get("meetingid")),
                    info.get("name"),
                    info.get("description"),
                    RoomDatabaseHandler.GetInstance().get(Integer.parseInt(info.get("Room_roomid"))),
                    DatabaseManager.stringToDateTime(info.get("start_time")),
                    DatabaseManager.stringToDateTime(info.get("end_time")),
                    info.get("location"),
                    new ArrayList<Entity>(),
                    UserDatabaseHandler.GetInstance().get(Integer.parseInt(info.get("owner_id")))
            );
            lgr.log(Level.INFO, "Meeting successfully generated");
            return meeting;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public int getNextId() {
        return 1; // TODO actually ask the database for next id
    }

    public Meeting get(int id) {
        try {
            lgr.log(Level.INFO, "Trying to get meeting by id: " + id);
            HashMap<String, String> info = DatabaseManager.getRow(String.format("SELECT * FROM Meeting WHERE meetingid=%d", id));
            lgr.log(Level.INFO, "Trying to generate meeting from: " + info.toString());
            return generateMeeting(info);
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }


    public boolean delete(Meeting meeting) {
        try {
            lgr.log(Level.INFO, "Trying to delete meeting by id: " + meeting.getId());
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

    public void addUserToMeeting(Meeting meeting, User user) {
        try {
            lgr.log(Level.INFO, String.format("Trying to add User %s to Meeting %s", user.getUsername(), meeting.getName()));
            insertUserInvitedToMeeting(meeting.getId(), user.getId(), "Dette møtet er superviktig.");
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
            lgr.log(Level.INFO, String.format("Deleting User %s from Meeting %s", user.getUsername(), meeting.getName()));
            deleteUserInvitedToMeeting(meeting.getId(), user.getId());
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void deleteUserInvitedToMeeting(int meetingid, int userid) throws SQLException {
        DatabaseManager.deleteRow("User_invited_to_meeting", "meeting", "user", meetingid, userid);
    }

}
