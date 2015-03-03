package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.server.CalendarServer;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mchange.v2.c3p0.*;

public final class DatabaseManager {

    private static Logger lgr;

    private static ComboPooledDataSource cpds;

    /**
     * Initializes a specific database
     *
     * @param url      url to the database
     * @param user     username in the database
     * @param password user's password to the database
     */
    public static void init(String url, String database, String user, String password) {
        lgr = Logger.getLogger(CalendarServer.class.getName());

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
    }

    /**
     * Initializes a default database
     *
     */
    static {
        init("mysql.stud.ntnu.no", "hermanmk_calDB", "hermanmk_cal", "cal123");
    }

    public static Connection createConnection() {
        try {
            Connection con = cpds.getConnection();
            lgr.log(Level.INFO, con.toString());
            return con;
            //con = DriverManager.getConnection(url, user, password);
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
            Statement st = createConnection().createStatement();
            return st;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
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
            lgr.log(Level.INFO, "Executing query: " + query);
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
        Statement st = getStatement();
        try {
            lgr.log(Level.INFO, "Executing query: " + query);
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
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
    public static void updateQuery(String query) {
        Statement st = getStatement();
        try {
            lgr.log(Level.INFO, "Executing query: " + query);
            st.executeUpdate(query);
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Executes a SQL query, and returns a HashMap with the columns specified in the query.
     * Should also work when using the 'AS' keyword because it gets the labels and not names.
     *
     * @param query
     * @return a HashMap<String, String> with the column labels as keys.
     */
    public static HashMap<String, String> getRow(String query) {
        HashMap<String, String> result = new HashMap<String, String>();
        ResultSet rs = readQuery(query);
        try {
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    result.put(rsmd.getColumnLabel(i), rs.getString(i));
                }
            }
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                rs.getStatement().getConnection().close();
            } catch (Exception ex) {
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
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
        try {
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                HashMap<String, String> row = new HashMap<String, String>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row.put(rsmd.getColumnLabel(i), rs.getString(i));
                }
                result.add(row);
            }
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
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

    // TODO virker ikke, gir bare 0 nå
    public static int getAutoIncrement(String table) {
        try {
            ResultSet rs = readQuery(String.format(
                    "SELECT last_insert_id() AS last_id " +
                            "FROM %s;", table));
            if (rs.next()) {
                lgr.log(Level.INFO, String.format("Next increment id for %s is %d", table, rs.getInt("last_id")));
                return rs.getInt("last_id");
            } else {
                return -1;
            }
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return -1;
        }
    }

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
}
