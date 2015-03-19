package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.models.GroupNotification;
import org.fellesprosjekt.gruppe24.common.models.Room;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GroupNotificationHandler extends DatabaseHandler<GroupNotification> {
    private String fromTable = "User_group_has_User";
    private static Logger lgr = Logger.getLogger(GroupDatabaseHandler.class.getName());
    UserDatabaseHandler uhandler = UserDatabaseHandler.GetInstance();
    GroupDatabaseHandler ghandler = GroupDatabaseHandler.GetInstance();

    private static GroupNotificationHandler instance;

    /**
     * Singleton-pattern.
     * Les på wikipedia, eller se `CalendarClient.java`
     *
     * @return Objektet.
     */
    public static GroupNotificationHandler GetInstance() {
        if (instance == null) return new GroupNotificationHandler();
        else return instance;
    }

    public GroupNotification generateGroupNotification(HashMap<String, String> info) {
        try {
            GroupNotification notification = new GroupNotification(
                    uhandler.get(Integer.parseInt(info.get("User_userid"))),
                    info.get("notification_message"),
                    Boolean.parseBoolean(info.get("notification_read")),
                    Boolean.parseBoolean(info.get("confirmed")),
                    ghandler.get(Integer.parseInt(info.get("User_group_groupid")))
            );
            return notification;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Notifications are generated automatically when creating a meeting, so this one is not implemented until it is
     * needed
     *
     * @param notification
     * @return null
     */
    @Override
    public GroupNotification insert(GroupNotification notification) {
        return null;
    }

    /**
     * This one cannot be used, and returns only null, since you need both userId and meetingId
     *
     * @param id Id-en til objektet.
     * @return null
     */
    @Override
    public GroupNotification get(int id) {
        return null;
    }

    public GroupNotification get(int userId, int groupId) {
        String query = String.format(
                "SELECT * FROM %s WHERE User_userid = %s AND User_group_groupid = %s;", fromTable, userId, groupId);
        try {
            HashMap<String, String> info = DatabaseManager.getRow(String.format(query));
            return generateGroupNotification(info);
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public List<GroupNotification> getAll() {
        List<GroupNotification> result = new ArrayList<GroupNotification>();
        String query = String.format("SELECT * FROM %s", fromTable);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row : resultSet) {
            result.add(generateGroupNotification(row));
        }
        return result;
    }

    public List<GroupNotification> getAllOfUser(int userId) {
        List<GroupNotification> result = new ArrayList<GroupNotification>();
        String query = String.format("SELECT * FROM %s WHERE User_userid = %s", fromTable, userId);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row : resultSet) {
            GroupNotification n = generateGroupNotification(row);
            if (n != null)
                result.add(n);
        }
        return result;
    }

    public List<GroupNotification> getAllOfGroup(int groupId) {
        List<GroupNotification> result = new ArrayList<GroupNotification>();
        String query = String.format("SELECT * FROM %s WHERE User_group_groupid = %s", fromTable, groupId);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row : resultSet) {
            GroupNotification n = generateGroupNotification(row);
            if (n != null)
                result.add(n);
        }
        return result;
    }

    @Override
    public boolean delete(GroupNotification notification) {
        return ghandler.removeUserFromGroup(notification.getUser().getId(), notification.getGroup().getId());
    }

    @Override
    public boolean update(GroupNotification notification) {
        String query = "" +
                "UPDATE %s SET notification_message = %s, notification_read = ?, confirmed = ? " +
                "WHERE User_userid = %s AND User_group_groupid = ?;";
        PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
        try {
            ps.setString(1, fromTable);
            ps.setString(2, notification.getMessage());
            ps.setBoolean(3, notification.isRead());
            ps.setBoolean(4, notification.isConfirmed());
            ps.setInt(5, notification.getUser().getId());
            ps.setInt(6, notification.getGroup().getId());
            return DatabaseManager.executePS(ps) != -1;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }
}
