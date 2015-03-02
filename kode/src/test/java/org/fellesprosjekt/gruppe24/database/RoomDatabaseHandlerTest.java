package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.junit.Test;

import java.util.List;

/**
 * Created by viktor on 02.03.15.
 */
public class RoomDatabaseHandlerTest extends TestCase {
    public void setUp() {

    }

    public void testCanGetRoomById() {
        Room room = RoomDatabaseHandler.getById(1);
        TestCase.assertNotNull(room);
        TestCase.assertNotNull(room.getName());
    }
    @Test(expected = java.sql.SQLException.class)
    public void testCanInsertAndDeleteRoom() {
        Room room = new Room("nytt_rom_fra_test", 4, true);
        Room room2 = RoomDatabaseHandler.getById(room.getId());
        TestCase.assertTrue(room.equals(room2));
        RoomDatabaseHandler.deleteById(room.getId());
        Room room3 = RoomDatabaseHandler.getById(room.getId());
    }
    public void testCanNotInsertCorruptRoom() {
        // valideringa tas av når man lager objektet, tror jeg
    }

    public void testCanGetAllRooms() {
        List allRooms = RoomDatabaseHandler.getAllRooms();
        TestCase.assertNotNull(allRooms);
    }

    public void testCanUpdateRoom() {
        Room room = RoomDatabaseHandler.getById(1);
        TestCase.assertTrue(DatabaseManager.updateField(1, "room_num", "Room", "Nytt_romnavn"));
        Room room2 = RoomDatabaseHandler.getById(1);
        TestCase.assertTrue(room2.getName().equals("Nytt_romnavn"));
        TestCase.assertTrue(DatabaseManager.updateField(1, "room_num", "Room", "Enda nyere romnavn"));
        room2 = RoomDatabaseHandler.getById(1);
        TestCase.assertTrue(room2.getName().equals("Enda nyere romnavn"));
    }
}
