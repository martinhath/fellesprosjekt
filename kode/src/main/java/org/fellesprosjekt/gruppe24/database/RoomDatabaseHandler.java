package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RoomDatabaseHandler {
    private static Logger lgr = Logger.getLogger(RoomDatabaseHandler.class.getName());

    public static ArrayList<Room> getAllRooms() {
        ArrayList<Room> allRooms = new ArrayList<Room>();
        String query = "SELECT * FROM Room;";
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
        for (HashMap<String, String> hm : result) {
            allRooms.add(generateRoom(hm));
        }
        return allRooms;
    }

    /**
     * Inserts a room in the database based on a room object
     *
     * @param room
     *
     * return int The database id the new room gets
     */
    public static int insertRoom(Room room) {
        try {
            String query =
                            "INSERT INTO Room " +
                            "(room_num, capacity) " +
                            "VALUES (?, ?);";
            PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
            ps.setString(1, room.getName());
            ps.setInt(2, room.getCapacity());
            //ps.setBoolean(3, room.isAccessible());
            // gikk ikke an å sette boolean-field ...
            return DatabaseManager.executePS(ps);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return -1;
        }
    }

    private static Room generateRoom(HashMap<String, String> info) {
        try {
            lgr.log(Level.INFO, "Generating room object based on: " + info.toString());
            Room room = new Room(
                    Integer.parseInt(info.get("roomid")),
                    info.get("room_num"),
                    Integer.parseInt(info.get("capacity")),
                    Integer.parseInt(info.get("accessible")) == 1
            );
            lgr.log(Level.INFO, "Room successfully generated");
            return room;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public static int getNextId() {
        return 1; // TODO actually ask the database for next id
    }

    public static Room getById(int id) {
        try {
            lgr.log(Level.INFO, "Trying to get room by id: " + id);
            HashMap<String, String> info = DatabaseManager.getRow(String.format("SELECT * FROM Room WHERE roomid=%d", id));
            lgr.log(Level.INFO, "Trying to generate room from: " + info.toString());
            return generateRoom(info);
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    public static boolean deleteById(int id) {
        try {
            lgr.log(Level.INFO, "Trying to delete room by id: " + id);
            DatabaseManager.updateQuery(String.format("DELETE FROM Room WHERE roomid=%d", id));
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }


}
