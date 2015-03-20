package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        if (instance == null)
            instance = new RoomDatabaseHandler();
        return instance;
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
                            "(room_num, capacity, available) " +
                            "VALUES (?, ?, ?);";
            PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
            ps.setString(1, room.getName());
            ps.setInt(2, room.getCapacity());
            ps.setBoolean(3, room.isAccessible());
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
            Room room = new Room(
                    Integer.parseInt(info.get("roomid")),
                    info.get("room_num"),
                    Integer.parseInt(info.get("capacity")),
                    Integer.parseInt(info.get("available")) == 1
            );
            return room;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public int getNextId() {
        return 1; // TODO actually ask the database for next id
    }

    public Room get(int id) {
        try {
            HashMap<String, String> info = DatabaseManager.getRow(
                    String.format("SELECT * FROM Room WHERE roomid=%d", id));
            return generateRoom(info);
        } catch (NumberFormatException|SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    public boolean delete(Room room) {
        try {
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
        int accessible = room.isAccessible() ? 1 : 0;
        try {
            ps.setInt(1, room.getCapacity());
            ps.setString(2, room.getName());
            ps.setBoolean(3, room.isAccessible());
            ps.setInt(4, room.getId());
            DatabaseManager.executePS(ps);
            return true;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    public List<Room> getAvailableRooms(Meeting meeting) {
        // finner alle rom som er åpne og som har kapasitet til møtet
        List<Room> result = new ArrayList<Room>();
        List<Room> excludedRooms = new ArrayList<Room>();
        String query = String.format(
                "SELECT * FROM Room WHERE capacity >= %d AND available = 1;",
                meeting.getParticipants().size());
        List<HashMap<String, String>> rooms = DatabaseManager.getList(query);

        for (HashMap<String, String> hm : rooms) {
            result.add(generateRoom(hm));
        }
        System.out.println("Skal hente rom");

        // filtrerer rom som er opptatte
        for (Room room : result) {
            query = String.format(
                    "SELECT meetingid, start_time, end_time FROM Meeting WHERE Room_roomid = %s;", room.getId());
            List<HashMap<String, String>> meetings = DatabaseManager.getList(query);
            for (HashMap<String, String> interval : meetings) {
                // sjekker om møtet kolliderer
                if (!(DatabaseManager.stringToDateTime(interval.get("start_time")).isAfter(meeting.getTo()) ||
                        DatabaseManager.stringToDateTime(interval.get("end_time")).isBefore(meeting.getFrom()))) {
                    excludedRooms.add(room);
                }
            }
        }
        result.removeAll(excludedRooms);
        return result;
    }


}
