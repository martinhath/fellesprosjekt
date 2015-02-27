package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by viktor on 27.02.15.
 */
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
    public static boolean insertMeeting(Meeting meeting) {
        try {
            String query =
                    "INSERT INTO Meeting " +
                    "(name, description, start_time, end_time, Room_roomid, owner_id, Group_groupid) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
            ps.setString(1, meeting.getName());
            ps.setString(2, meeting.getDescription());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(meeting.getFrom()));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(meeting.getTo()));
            ps.setInt(5, meeting.getRoom().getID());
            ps.setInt(6, meeting.getOwner().getID());
            ps.setInt(7, 1); // TODO should be a groupID
            DatabaseManager.executePS(ps);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    private static Meeting generateMeeting(HashMap<String, String> info) {
        try {
            lgr.log(Level.INFO, "Generating meeting object based on: " + info.toString());
        Meeting meeting = new Meeting(
                Integer.parseInt(info.get("meetingid")),
                info.get("name"),
                info.get("description"),
        		new Room(), // TODO ordne at man faktisk henter det aktuelle rommet basert på en ID fra DB
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

}
