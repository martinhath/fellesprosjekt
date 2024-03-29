package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.*;

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

    public ArrayList<Meeting> getAll(User user) {
        return (ArrayList<Meeting>) UserDatabaseHandler.GetInstance().getMeetingsOfUser(user);
    }

    public ArrayList<Meeting> getAll(Group group) {
        return (ArrayList<Meeting>) GroupDatabaseHandler.GetInstance().getAllMeetingsForGroup(group.getId());
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
                        "(name, description, start_time, end_time, room_roomid, owner_id, group_groupid, location, create_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW());";
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
        ps.setString(8, " "); // vi har ikke location ?

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
            Room room = info.get("room_roomid") == null ?
                    null : RoomDatabaseHandler.GetInstance().get(Integer.parseInt(info.get("room_roomid")));
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
        int roomid = 0;
        int groupid = 0;
        int ownerid = 0;
        if (meeting.getRoom() != null) roomid = meeting.getRoom().getId();
        if (meeting.getGroup() != null) groupid = meeting.getGroup().getId();
        if (meeting.getOwner() != null) ownerid = meeting.getOwner().getId();
        try {
            String query = String.format(
                    "UPDATE Meeting SET name = ?, " +
                            "description = ?, " +
                            "start_time = ?, " +
                            "end_time = ?, " +
                            "room_roomid = ?, " +
                            "location = ?, " +
                            "owner_id = ?, " +
                            "group_groupid = ?"
                            + " WHERE meetingid = ?;");
            PreparedStatement ps = DatabaseManager.getPreparedStatement(query);

            ps.setString(1, meeting.getName());
            ps.setString(2, meeting.getDescription());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(meeting.getFrom()));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(meeting.getTo()));
            if (roomid == 0) ps.setNull(5, 0);
            else ps.setInt(5, roomid);
            ps.setString(6, meeting.getLocation());
            if (ownerid == 0) ps.setNull(7, 0);
            else ps.setInt(7, ownerid);
            if (groupid == 0) ps.setNull(8, 0);
            else ps.setInt(8, groupid);
            ps.setInt(9, meeting.getId());
            return DatabaseManager.executePS(ps) != -1;
        } catch (Exception ex) {
            lgr.severe(ex.toString());
            return false;
        }
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
                "(user_userid, meeting_meetingid, notification_message) " +
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
        String query = String.format("SELECT user_userid FROM User_invited_to_meeting WHERE meeting_meetingid=%d", meetingid);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row : resultSet) {
            result.add(Integer.parseInt(row.get("user_userid")));
        }
        return result;
    }

    public boolean removeUserFromMeeting(Meeting meeting, User user) {
        try {
            return deleteUserInvitedToMeeting(meeting.getId(), user.getId());
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    private boolean deleteUserInvitedToMeeting(int meetingid, int userid) throws SQLException {
        return DatabaseManager.deleteRow("User_invited_to_meeting", "meeting", "user", meetingid, userid);
    }

}
