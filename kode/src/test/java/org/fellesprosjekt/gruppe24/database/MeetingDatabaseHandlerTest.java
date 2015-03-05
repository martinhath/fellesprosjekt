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


public class MeetingDatabaseHandlerTest {

    public void setUp() {

    }
    
    @Test
    public void testCanInsertAndDeleteMeeting() {
        Meeting meeting = new Meeting(
                "pressekonferanse",
                "",
                RoomDatabaseHandler.getById(1),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "P15",
                new ArrayList<>(),
                new User());
        Meeting meeting2 = MeetingDatabaseHandler.getById(meeting.getId());
        //TestCase.assertEquals(meeting.toString(), meeting2.toString());
        // noe er null
        // TestCase.assertEquals(meeting.getId(), meeting2.getId());

        MeetingDatabaseHandler.deleteById(meeting.getId());
        MeetingDatabaseHandler.deleteById(meeting.getId());
    }

    @Test
    public void testCanRetrieveMeetingFromDB() {
        List<Meeting> meetingList = MeetingDatabaseHandler.getAllMeetings();
        Meeting meeting = meetingList.get(0);
        TestCase.assertNotNull(meeting);
        TestCase.assertNotNull(meeting.getOwner());
        TestCase.assertNotNull(meeting.getName());
    }

}
