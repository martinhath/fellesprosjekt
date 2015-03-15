package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MeetingDatabaseHandlerTest {
    private User user;
    private Room room;

    private UserDatabaseHandler uhandler;
    private MeetingDatabaseHandler mhandler;
    private RoomDatabaseHandler rhandler;

    @Before
    public void before() {
        uhandler = UserDatabaseHandler.GetInstance();
        mhandler = MeetingDatabaseHandler.GetInstance();
        rhandler = RoomDatabaseHandler.GetInstance();

        user = uhandler.getAll().get(0);
        Meeting meeting = mhandler.insert(new Meeting(
                "Fredagspils",
                "Som vanlig ses vi på fredag.",
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "Freddy`s bar",
                new ArrayList<>(),
                user));
        mhandler.addUserToMeeting(meeting, user);
        room = rhandler.insert(new Room("Mororommet", 8, true));
    }

    @After
    public void after() {
        User user = uhandler.getAll().get(0);

        Meeting meeting = mhandler.getAll().get(0);
        mhandler.removeUserFromMeeting(meeting, user);
        mhandler.delete(meeting);
    }
    
    @Test
    public void testCanInsertAndDeleteMeeting() {
        Meeting meeting = mhandler.insert(
                new Meeting(
                "pressekonferanse",
                "",
                room,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "P15",
                new ArrayList<>(),
                user));
        Meeting meeting2 = mhandler.get(meeting.getId());
        assertEquals(meeting.getId(), meeting2.getId());

        mhandler.delete(meeting);
        assertNull(mhandler.get(meeting.getId()));
        mhandler.delete(meeting);
    }

    @Test
    public void testCanRetrieveMeetingFromDB() {
        List<Meeting> meetingList = mhandler.getAll();
        Meeting meeting = meetingList.get(0);
        assertNotNull(meeting);
        assertNotNull(meeting.getOwner());
        assertNotNull(meeting.getName());
    }

    @Test
    public void testCanAddAndRemoveUserToMeeting() {
        User user = uhandler.getAll().get(1);
        Meeting meeting = mhandler.getAll().get(0);
        int sizeBefore = mhandler.getUsersOfMeeting(meeting).size();

        mhandler.addUserToMeeting(meeting, user);
        assertTrue(mhandler.getUsersOfMeeting(meeting).size() > sizeBefore);

        mhandler.removeUserFromMeeting(meeting, user);
        assertEquals(sizeBefore, mhandler.getUsersOfMeeting(meeting).size());
    }

    @Test
    public void testCanGetUsersOfMeeting() {
        Meeting meeting = mhandler.get(2);   // Gruppemøte
        List<User> users = mhandler.getUsersOfMeeting(meeting);
        assertNotNull(users);
        assertNotEquals(users.size(), 0);
    }
}
