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

    public void testCanInsertMeeting() {
        Meeting meeting = new Meeting(
                "fredagspils",
                "",
                new Room(),
                LocalDateTime.now(),
                LocalDateTime.now().plusWeeks(1),
                "P15",
                new ArrayList<User>(),
                new User());
        MeetingDatabaseHandler.insertMeeting(meeting);

        String expected = "fredagspils";
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList("SELECT * FROM Meeting;");
        System.out.println("ALL MEETINGS: " + result);
        String actual = result.get(0).get("name");
        TestCase.assertEquals(expected, actual);
    }

}
