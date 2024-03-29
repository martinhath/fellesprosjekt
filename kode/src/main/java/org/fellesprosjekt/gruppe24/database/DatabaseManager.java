package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.server.CalendarServer;

import java.beans.PropertyVetoException;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mchange.v2.c3p0.*;

public final class DatabaseManager {

    private static Logger lgr = Logger.getLogger(CalendarServer.class.getName());

    private static ComboPooledDataSource cpds;

    private static boolean isInit = false;

    public enum Type {PROD, TEST}

    public static void init(Type t) {
        if (isInit) {
            lgr.warning("Database is already initialized!");
            return;
        }
        isInit = true;
        switch (t) {
            case PROD:
                init_prod("mysql.stud.ntnu.no", "hermanmk_calDB", "hermanmk_cal", "cal123");
                break;
            case TEST:
                init_test();

        }
    }

    public static void init_prod(String url, String database, String user, String password) {
        if (!isInit) return;
        // Connection Pooling
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        //loads the jdbc driver            
        cpds.setJdbcUrl(String.format("jdbc:mysql://%s:3306/%s", url, database));
        cpds.setUser(user);
        cpds.setPassword(password);

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20); // klikka under tester da den bare var 20
        cpds.setMaxIdleTime(1);

        try {
            Connection c = cpds.getConnection();
            c.close();
        } catch (SQLException e) {
            lgr.severe("Failed to get an initial connection");
            System.exit(69);
        }
        lgr.info("Init done.");
    }

    public static void init_test() {
        if (!isInit) return;
        cpds = new ComboPooledDataSource();
        try {
            //cpds.setDriverClass("com.mysql.jdbc.Driver");
            cpds.setDriverClass("org.h2.Driver");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setAcquireRetryAttempts(5);
        cpds.setAcquireRetryDelay(200);
        cpds.setMaxPoolSize(20);
        cpds.setMaxIdleTime(1);

        cpds.setJdbcUrl("jdbc:h2:mem:test_database;MODE=MySQL;IGNORECASE=true;DB_CLOSE_DELAY=-1;" +
                "INIT=runscript from " +
                "'../docs/database_script.sql'\\;runscript from '../docs/database_data.sql'");
        try {
            Connection c = cpds.getConnection();
            c.close();
        } catch (SQLException e) {
            lgr.severe("Failed to get an initial connection");
            System.exit(69);
        }
        lgr.info("Init done.");
    }

    public static Connection createConnection() {
        try {
            Connection con = cpds.getConnection();
            return con;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public static PreparedStatement getPreparedStatement(String query) {
        try {
            Connection con = createConnection();
            // RETURN GENERATED KEYS makes it possible to get the ID of a newly inserted row
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            return ps;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * @param ps the PreparedStatement to execute
     * @return an integer representing the key added to the database row if inserted
     */
    public static int executePS(PreparedStatement ps) {
        try {
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return -1;
        } finally {
            try {
                ps.getConnection().close();
            } catch (Exception ex) {
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return -1;
    }

    public static Statement getStatement() {
        try {
            Connection con = createConnection();
            Statement st = con.createStatement();
            return st;
        } catch (SQLException ex) {
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * A very basic select query
     *
     * @param select which column to select
     * @param from   which table to select from
     * @return a string representing the value of desired cell
     */
    public static String getCell(String select, String from) {
        Statement st = getStatement();
        try {
            String query = String.format("SELECT * FROM %s;", from);
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                return rs.getString(select);
            }

        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                st.getConnection().close();
            } catch (Exception ex) {
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return null;
    }

    /**
     * SQL Query that do not alter the database. Eg. SELECT queries
     *
     * @param query
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet readQuery(String query) {
        try {
            Statement st = getStatement();
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage());
        }
        return null;
    }

    /**
     * SQL Query that alters the database. Eg. CREATE TABLE, INSERT, UPDATE,
     * DELETE query.
     *
     * @param query
     * @throws SQLException
     */
    public static boolean updateQuery(String query) {
        Statement st = getStatement();
        boolean ret = false;
        try {
            st.executeUpdate(query);
            ret =  true;
            st.getConnection().close();
            st.close();
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return ret;
    }

    public static HashMap<String, String> getRow(String query) throws SQLException {
        HashMap<String, String> result = new HashMap<String, String>();
        ResultSet rs = readQuery(query);
        if (rs == null)
            return null;
        if (rs.next()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                result.put(rsmd.getColumnLabel(i).toLowerCase(), rs.getString(i));
            }
        }
        try {
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * Executes a SQL query, and returns an ArrayList with all the resulting rows.
     * Useful when querying for a list of objects.
     * Should also work when using the 'AS' keyword because it gets the labels and not names.
     *
     * @param query
     * @return an ArrayList containing all the rows as nested HashMap<String, String>
     */
    protected static ArrayList<HashMap<String, String>> getList(String query) {
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        ResultSet rs = readQuery(query);
        if (rs == null) return null;
        try {
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                HashMap<String, String> row = new HashMap<String, String>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row.put(rsmd.getColumnLabel(i).toLowerCase(), rs.getString(i));
                }
                result.add(row);
            }
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                Statement s = rs.getStatement();
                Connection c = s.getConnection();
                c.close();
            } catch (Exception ex) {
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return result;
    }

    /**
     * Takes a string of a timestamp like one from a SQL DateTime Field and returns a LocalDateTime object
     * To be used when getting raw strings from a SQL select query
     *
     * @param timestamp string of a timestamp like SQL DateTime
     * @return LocalDateTime object
     */

    public static LocalDateTime stringToDateTime(String timestamp) {
        try {
            return java.sql.Timestamp.valueOf(timestamp).toLocalDateTime();
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, "Could not convert timestamp to LocalDateTime");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
    /**
     * Takes a string of a timestamp like one from a SQL Time Field and returns a LocalTime object
     * To be used when getting raw strings from a SQL select query
     *
     * @param timestamp string of a timestamp like SQL Time
     * @return LocalTime object
     */

    public static LocalTime stringToTime(String timestamp) {
        try {
            return java.sql.Time.valueOf(timestamp).toLocalTime();
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, "Could not convert timestamp to LocalTime");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    @Deprecated
    public static int getLastId(String table) {
            ResultSet rs = readQuery(String.format(
                    "SELECT LAST_INSERT_ID() AS last_id " +
                            "FROM %s;", table));
        try {
            if (rs.next()) {
                return rs.getInt("last_id");
            } else {
                return -1;
            }
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return -1;
        } finally {
            try {
                rs.getStatement().getConnection().close();
            } catch (Exception ex) {
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Updates one field in a table that can be represented by a String
     *
     * @param id
     * @param field
     * @param table
     * @param newValue
     * @return
     */
    public static boolean updateField(int id, String field, String table, String newValue) {
        try {
            PreparedStatement ps = getPreparedStatement(String.format(
                    "UPDATE %s " +
                            "SET %s = ? " +
                            "WHERE %sid=?", table, field, table));
            ps.setString(1, newValue);
            ps.setInt(2, id);
            executePS(ps);
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }

    }

    /**
     * Updates one field in a table that can be represented by an Integer
     *
     * @param id
     * @param field
     * @param table
     * @param newValue
     * @return
     */
    public static boolean updateField(int id, String field, String table, int newValue) {
        try {
            PreparedStatement ps = getPreparedStatement(String.format(
                    "UPDATE %s " +
                            "SET %s = ? " +
                            "WHERE %sid=?", table, field, table));
            ps.setInt(1, newValue);
            ps.setInt(2, id);
            executePS(ps);
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Updates one field in a table that can be represented by a LocalDateTime
     *
     * @param id
     * @param field
     * @param table
     * @param newValue
     * @return
     */
    public static boolean updateField(int id, String field, String table, LocalDateTime newValue) {
        try {
            PreparedStatement ps = getPreparedStatement(String.format(
                    "UPDATE %s " +
                            "SET %s = ? " +
                            "WHERE %sid=?", table, field, table));
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(newValue));
            ps.setInt(2, id);
            executePS(ps);
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    public static boolean updateField(
            int pk1, int pk2, String table, String field, String foreignTable1, String foreignTable2, boolean newValue)
            throws SQLException {
        try {
            PreparedStatement ps = getPreparedStatement(String.format(
                    "UPDATE %s " +
                            "SET %s = ? " +
                            "WHERE %s_%sid=? " +
                            "AND %s_%sid=?", table, field, foreignTable1, foreignTable1, foreignTable2, foreignTable2));
            ps.setBoolean(1, newValue);
            ps.setInt(2, pk1);
            ps.setInt(3, pk2);
            executePS(ps);
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    public static boolean deleteRow(String table, int id) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE %sid=?", table, table);
        PreparedStatement ps = getPreparedStatement(query);
        ps.setInt(1, id);
        return executePS(ps) != -1; // executePS gir -1 hvis den var mislykket
    }

    public static boolean deleteRow(
            String table, String foreignTable1, String foreignTable2, int fk1, int fk2) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE %s_%sid=? AND %s_%sid=?",
                table, foreignTable1, foreignTable1, foreignTable2, foreignTable2);
        PreparedStatement ps = getPreparedStatement(query);
        ps.setInt(1, fk1);
        ps.setInt(2, fk2);
        return executePS(ps) != -1; // executePS gir -1 hvis den var mislykket
    }

}
