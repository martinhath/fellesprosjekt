package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Meeting;

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

    public static ArrayList<Meeting> getAllMeetings() {
        ArrayList<Meeting> allMeetings = new ArrayList<Meeting>();
        String query = "SELECT * FROM Meeting;";
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
        for (HashMap<String, String> hm : result) {
            //allMeetings.add(generateMeeting(hm));
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
            Connection con = DatabaseManager.createConnection();
            String query = "INSERT INTO Meeting " +
                    "(name, description, start_time, end_time, Room_roomid, location, owner_id, Group_groupid) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, meeting.getName());
            ps.setTime(3, java.sql.Time.valueOf(meeting.getFrom().toLocalTime()));
            ps.setTime(4, java.sql.Time.valueOf(meeting.getTo().toLocalTime()));
            ps.setString(2, meeting.getDescription());
            ps.setString(6, meeting.getLocation());
            ps.setInt(5, meeting.getRoom().getId());
            ps.setInt(7, meeting.getOwner().getId());
            ps.setInt(8, meeting.getGroup().getId());
            return ps.execute();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }
    /*
    private static Meeting generateMeeting(HashMap<String, String> info) {
        //Meeting meeting = new Meeting(info.get("Meetingid"), info.get("Meetingname"), info.get("email"), info.get("password"),
        //		info.get("create_time"), info.get("update_time"));
        // TODO: Add proper constructor so the previous lines can be uncommented.
        Meeting meeting = new Meeting(info.get("Meetingname"));
        return meeting;
    }
    */

    public static int getNextId() {
        return 1; // TODO actually ask the database for next id
    }

}
