package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MeetingDatabaseHandler {
    private static Logger lgr = Logger.getLogger(MeetingDatabaseHandler.class.getName());

    public static ArrayList<Meeting> getAllMeetings() {
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
    public static int insertMeeting(Meeting meeting) {
        try {
            String query =
                    "INSERT INTO Meeting " +
                    "(name, description, start_time, end_time, Room_roomid, owner_id, Group_groupid, location) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
            ps.setString(1, meeting.getName());
            ps.setString(2, meeting.getDescription());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(meeting.getFrom()));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(meeting.getTo()));
            ps.setInt(5, meeting.getRoom().getId());
            ps.setInt(6, meeting.getOwner().getId());
            ps.setInt(7, 1); // TODO should be a groupID
            ps.setString(8, meeting.getLocation());
            DatabaseManager.executePS(ps);
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                lgr.log(Level.INFO, "Meeting created with id: " + rs.getInt(1));
                return rs.getInt(1);
            } else return -1;
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return -1;
        }
    }

    private static Meeting generateMeeting(HashMap<String, String> info) {
        try {
            lgr.log(Level.INFO, "Generating meeting object based on: " + info.toString());
        Meeting meeting = new Meeting(
                Integer.parseInt(info.get("meetingid")),
                info.get("name"),
                info.get("description"),
        		RoomDatabaseHandler.getById(Integer.parseInt(info.get("Room_roomid"))),
                DatabaseManager.stringToDateTime(info.get("start_time")),
                DatabaseManager.stringToDateTime(info.get("end_time")),
                info.get("location"),
                new ArrayList<User>(),
                new User()
        );
        lgr.log(Level.INFO, "Meeting successfully generated");
        return meeting;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public static int getNextId() {
        return 1; // TODO actually ask the database for next id
    }

    public static Meeting getById(int id) {
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
    public static boolean deleteById(int id) {
        try {
            lgr.log(Level.INFO, "Trying to delete meeting by id: " + id);
            DatabaseManager.updateQuery(String.format("DELETE FROM Meeting WHERE meetingid=%d", id));
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

}
