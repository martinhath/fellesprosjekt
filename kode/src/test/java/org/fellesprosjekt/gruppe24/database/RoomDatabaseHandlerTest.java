package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.junit.Test;

import java.util.List;

public class RoomDatabaseHandlerTest extends TestCase {
    public void setUp() {

    }

    public void testCanGetRoomById() {
        Room room = RoomDatabaseHandler.GetInstance().get(1);
        TestCase.assertNotNull(room);
        TestCase.assertNotNull(room.getName());
    }
    @Test(expected = java.sql.SQLException.class)
    public void testCanInsertAndDeleteRoom() {
        Room room = new Room("nytt_rom_fra_test", 4, true);
        Room room2 = RoomDatabaseHandler.GetInstance().get(room.getId());
        TestCase.assertTrue(room.equals(room2));
        RoomDatabaseHandler.GetInstance().delete(room);
        Room room3 = RoomDatabaseHandler.GetInstance().get(room.getId());
    }
    public void testCanNotInsertCorruptRoom() {
        // valideringa tas av når man lager objektet, tror jeg
    }

    public void testCanGetAllRooms() {
        List allRooms = RoomDatabaseHandler.GetInstance().getAll();
        TestCase.assertNotNull(allRooms);
    }

    public void testCanUpdateRoom() {
        Room room = RoomDatabaseHandler.GetInstance().get(1);
        TestCase.assertTrue(DatabaseManager.updateField(1, "room_num", "Room", "Nytt_romnavn"));
        Room room2 = RoomDatabaseHandler.GetInstance().get(1);
        TestCase.assertTrue(room2.getName().equals("Nytt_romnavn"));
        TestCase.assertTrue(DatabaseManager.updateField(1, "room_num", "Room", "Enda nyere romnavn"));
        room2 = RoomDatabaseHandler.GetInstance().get(1);
        TestCase.assertTrue(room2.getName().equals("Enda nyere romnavn"));
    }
}
