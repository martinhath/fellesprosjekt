package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by viktor on 27.02.15.
 */

public class MeetingDatabaseHandlerTest extends TestCase {
    public void setUp() {

    }
    
    @Test(expected = java.sql.SQLException.class)
    public void testCanInsertAndDeleteMeeting() {
        Meeting meeting = new Meeting(
                "pressekonferanse",
                "",
                RoomDatabaseHandler.getById(1),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "P15",
                new ArrayList<User>(),
                new User());
        Meeting meeting2 = MeetingDatabaseHandler.getById(meeting.getID());
        TestCase.assertEquals(meeting.toString(), meeting2.toString());

        Meeting expected = meeting;
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList(
                "SELECT * " +
                "FROM Meeting " +
                "WHERE name='pressekonferanse';");
        System.out.println("MEETING: " + result);
        HashMap<String, String> actual = result.get(0);
        TestCase.assertEquals(expected.getDescription(), actual.get("description"));
        TestCase.assertEquals(expected.getID(), Integer.parseInt(actual.get("meetingid")));
        TestCase.assertEquals(expected.getFrom(), DatabaseManager.stringToDateTime(actual.get("end_time")));
        MeetingDatabaseHandler.deleteById(meeting.getID());
        MeetingDatabaseHandler.deleteById(meeting.getID());
    }

    public void testCanRetrieveMeetingFromDB() {
        List<Meeting> meetingList = MeetingDatabaseHandler.getAllMeetings();
        System.out.println("ALL MEETINGS: " + meetingList);
        Meeting meeting = meetingList.get(0);
        TestCase.assertNotNull(meeting);
        TestCase.assertNotNull(meeting.getOwner());
        TestCase.assertNotNull(meeting.getName());
    }

}
