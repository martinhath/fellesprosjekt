package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Room;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RoomDatabaseHandlerTest {

    private Room room;

    private RoomDatabaseHandler rhandler;

    @Before
    public void setUp() {
        rhandler = RoomDatabaseHandler.GetInstance();
        room = rhandler.insert(new Room("Mororommet", 8, true));
    }

    @Test
    public void testCanGetRoomById() {
        Room room2 = rhandler.get(room.getId());
        assertNotNull(room);
        assertNotNull(room.getName());
        assertEquals(room.getId(), room2.getId());
    }
    @Test
    public void testCanInsertAndDeleteRoom() {
        Room room2 = rhandler.insert(new Room("nytt_rom_fra_test", 4, true));
        Room room3 = rhandler.get(room2.getId());
        assertEquals(room2.getId(), room3.getId());
        rhandler.delete(room2);

        rhandler.get(room2.getId());
    }

    @Test
    public void testCanGetAllRooms() {
        List allRooms = rhandler.getAll();
        assertNotNull(allRooms);
    }

    @Test
    public void testCanUpdateRoom() {
        boolean originalAccessible = room.isAccessible();
        room.setAccessible(!originalAccessible);
        rhandler.update(room);
        assertEquals(!originalAccessible, rhandler.get(room.getId()).isAccessible());
    }
}
