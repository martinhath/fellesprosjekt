package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.junit.Test;

import java.util.List;

public class RoomDatabaseHandlerTest extends TestCase {
    Room room;
    public void setUp() {
        room = RoomDatabaseHandler.GetInstance().insert(new Room("Mororommet", 8, true));
    }

    public void testCanGetRoomById() {
        Room room2 = RoomDatabaseHandler.GetInstance().get(room.getId());
        TestCase.assertNotNull(room);
        TestCase.assertNotNull(room.getName());
        TestCase.assertEquals(room.getId(), room2.getId());
    }
    @Test(expected = java.sql.SQLException.class)
    public void testCanInsertAndDeleteRoom() {
        Room room2 = RoomDatabaseHandler.GetInstance().insert(new Room("nytt_rom_fra_test", 4, true));
        Room room3 = RoomDatabaseHandler.GetInstance().get(room2.getId());
        TestCase.assertEquals(room2.getId(), room3.getId());
        RoomDatabaseHandler.GetInstance().delete(room2);
        Room room4 = RoomDatabaseHandler.GetInstance().get(room2.getId());
    }

    public void testCanGetAllRooms() {
        List allRooms = RoomDatabaseHandler.GetInstance().getAll();
        TestCase.assertNotNull(allRooms);
    }

    public void testCanUpdateRoom() {
        boolean originalAccessible = room.isAccessible();
        room.setAccessible(!originalAccessible);
        RoomDatabaseHandler.GetInstance().update(room);
        TestCase.assertEquals(!originalAccessible, RoomDatabaseHandler.GetInstance().get(room.getId()).isAccessible());
    }
}
