package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;

import javax.xml.crypto.Data;
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
    public void testCanInsertMeeting() { // TODO a test should not add rows like permanently
        Meeting meeting = new Meeting(
                "pressekonferanse",
                "",
                new Room(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "P15",
                new ArrayList<User>(),
                new User());
        boolean inserted = MeetingDatabaseHandler.insertMeeting(meeting);
        TestCase.assertEquals(true, inserted);


        Meeting expected = meeting;
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList(
                "SELECT * " +
                "FROM Meeting " +
                "WHERE name='pressekonferanse';");
        System.out.println("MEETING: " + result);
        HashMap<String, String> actual = result.get(0);
        TestCase.assertEquals(expected.getDescription(), actual.get("description"));
        TestCase.assertEquals(expected.getId(), Integer.parseInt(actual.get("meetingid")));
        TestCase.assertEquals(expected.getFrom(), DatabaseManager.stringToDateTime(actual.get("end_time")));
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
