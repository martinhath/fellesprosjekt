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


public class MeetingDatabaseHandlerTest extends TestCase {
    public void setUp() {
        User user = UserDatabaseHandler.GetInstance().getAll().get(0);
        Meeting meeting = MeetingDatabaseHandler.GetInstance().getAll().get(0);
        MeetingDatabaseHandler.GetInstance().addUserToMeeting(meeting, user);
    }
    public void tearDown() {
        User user = UserDatabaseHandler.GetInstance().getAll().get(0);
        Meeting meeting = MeetingDatabaseHandler.GetInstance().getAll().get(0);
        MeetingDatabaseHandler.GetInstance().removeUserFromMeeting(meeting, user);
    }
    
    @Test(expected = java.sql.SQLException.class)
    public void testCanInsertAndDeleteMeeting() {
        Meeting meeting = new Meeting(
                "pressekonferanse",
                "",
                RoomDatabaseHandler.GetInstance().get(1),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "P15",
                new ArrayList<>(),
                new User());
        Meeting meeting2 = MeetingDatabaseHandler.GetInstance().get(meeting.getId());
        //TestCase.assertEquals(meeting.toString(), meeting2.toString());
        TestCase.assertEquals(meeting.getId(), meeting2.getId());

        MeetingDatabaseHandler.GetInstance().delete(meeting);
        MeetingDatabaseHandler.GetInstance().delete(meeting);
    }

    public void testCanRetrieveMeetingFromDB() {
        List<Meeting> meetingList = MeetingDatabaseHandler.GetInstance().getAll();
        Meeting meeting = meetingList.get(0);
        TestCase.assertNotNull(meeting);
        TestCase.assertNotNull(meeting.getOwner());
        TestCase.assertNotNull(meeting.getName());
    }

    public void testCanAddAndRemoveUserToMeeting() {
        User user = UserDatabaseHandler.GetInstance().getAll().get(1);
        Meeting meeting = MeetingDatabaseHandler.GetInstance().getAll().get(0);
        int sizeBefore = MeetingDatabaseHandler.GetInstance().getUsersOfMeeting(meeting).size();

        MeetingDatabaseHandler.GetInstance().addUserToMeeting(meeting, user);
        TestCase.assertTrue(MeetingDatabaseHandler.GetInstance().getUsersOfMeeting(meeting).size() > sizeBefore);

        MeetingDatabaseHandler.GetInstance().removeUserFromMeeting(meeting, user);
        TestCase.assertEquals(sizeBefore, MeetingDatabaseHandler.GetInstance().getUsersOfMeeting(meeting).size());
    }

    public void testCanGetUsersOfMeeting() {
        Meeting meeting = MeetingDatabaseHandler.GetInstance().getAll().get(0);
        List<User> users = MeetingDatabaseHandler.GetInstance().getUsersOfMeeting(meeting);
        TestCase.assertNotNull(users);
        TestCase.assertNotNull(users.get(0).getUsername());
    }
}
