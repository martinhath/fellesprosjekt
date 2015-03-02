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
        Room room2 = RoomDatabaseHandler.getById(room.getID());
        TestCase.assertTrue(room.equals(room2));
        RoomDatabaseHandler.deleteById(room.getID());
        Room room3 = RoomDatabaseHandler.getById(room.getID());
    }
    public void testCanNotInsertCorruptRoom() {
        // valideringa tas av når man lager objektet, tror jeg
    }

    public void testCanGetAllRooms() {
        List allRooms = RoomDatabaseHandler.getAllRooms();
        TestCase.assertNotNull(allRooms);
    }

}
