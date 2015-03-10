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

public final class RoomDatabaseHandler extends DatabaseHandler<Room> {
    private static Logger lgr = Logger.getLogger(RoomDatabaseHandler.class.getName());
    private static RoomDatabaseHandler instance;

    /**
     * Singleton-pattern.
     * Les på wikipedia, eller se `CalendarClient.java`
     *
     * @return Objektet.
     */
    public static RoomDatabaseHandler GetInstance() {
        if (instance == null) return new RoomDatabaseHandler();
        else return instance;
    }

    public ArrayList<Room> getAll() {
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
    public Room insert(Room room) {
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
            int newId = DatabaseManager.executePS(ps);
            return new Room(newId, room.getName(), room.getCapacity(), room.isAccessible());
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    private Room generateRoom(HashMap<String, String> info) {
        try {
            lgr.log(Level.INFO, "Generating room object based on: " + info.toString());
            Room room = new Room(
                    Integer.parseInt(info.get("roomid")),
                    info.get("room_num"),
                    Integer.parseInt(info.get("capacity")),
                    Integer.parseInt(info.get("available")) == 1
            );
            lgr.log(Level.INFO, "Room successfully generated");
            return room;
        } catch (NumberFormatException ex) {
            lgr.log(Level.INFO, "Could not generate room. Probably is null.");
            return null;
        }
    }

    public int getNextId() {
        return 1; // TODO actually ask the database for next id
    }

    public Room get(int id) {
        try {
            lgr.log(Level.INFO, "Trying to get room by id: " + id);
            HashMap<String, String> info = DatabaseManager.getRow(String.format("SELECT * FROM Room WHERE roomid=%d", id));
            lgr.log(Level.INFO, "Trying to generate room from: " + info.toString());
            return generateRoom(info);
        } catch (NumberFormatException|SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    public boolean delete(Room room) {
        try {
            lgr.log(Level.INFO, "Trying to delete room by id: " + room.getId());
            DatabaseManager.updateQuery(String.format("DELETE FROM Room WHERE roomid=%d", room.getId()));
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    public boolean update(Room room) {
        String query = String.format(
                "UPDATE Room SET capacity = ?, room_num = ?, available = ? WHERE roomid=?;");
        PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
        lgr.log(Level.INFO, String.format("Updating %s", room));
        int accessible = room.isAccessible() ? 1 : 0;
        try {
            ps.setInt(1, room.getCapacity());
            ps.setString(2, room.getName());
            ps.setBoolean(3, room.isAccessible());
            ps.setInt(4, room.getId());
            DatabaseManager.executePS(ps);
            System.out.println(accessible);
            //DatabaseManager.updateQuery(
            //        String.format("UPDATE Room SET accessible = 0 WHERE roomid = %s;", room.getId()));
            return true;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }


}
