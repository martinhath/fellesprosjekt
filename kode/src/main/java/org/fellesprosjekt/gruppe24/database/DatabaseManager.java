package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.server.Main;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {

    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;

    private String url;
    private String user;
    private String password;

    /**
     * Connects to a specific database
     *
     * @param url url to the database
     * @param user username in the database
     * @param password user's password to the database
     */
    public DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        connect();
    }

    /**
     * Connects to a default database
     *
     */
    public DatabaseManager() {
        this.url = "jdbc:mysql://mysql.stud.ntnu.no:3306/hermanmk_calDB";
        this.user = "hermanmk_cal";
        this.password = "cal123";
        connect();
    }

    /**
     * A very basic select query
     *
     * @param select which column to select
     * @param from which table to select from
     * @return a string representing the value of desired cell
     */
    public String getCell(String select, String from) {
        Logger lgr = Logger.getLogger(Main.class.getName());
        try {
            String query = String.format("SELECT * FROM %s;", from);
            lgr.log(Level.INFO, "Executing query: " + query);
            this.st = this.con.createStatement();
            this.rs = this.st.executeQuery(query);

            if (this.rs.next()) {
                return this.rs.getString(select);
            }

        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (this.rs != null) {
                    this.rs.close();
                }
                if (this.st != null) {
                    this.st.close();
                }
            } catch (SQLException ex) {
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return null;
    }

    /**
     * Creates a connection to a database and makes a statement ready to be used
     */
    private void connect() {
        Logger lgr = Logger.getLogger(Main.class.getName());
        try {
            this.con = DriverManager.getConnection(this.url, this.user, this.password);
            lgr.log(Level.INFO, this.con.toString());
            /*
            this.st = this.con.createStatement();
            this.rs = this.st.executeQuery("SELECT * FROM Room;");

            if (this.rs.next()) {
                System.out.println(this.rs.getString("room_num"));
            }
            */

        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void close() {
        Logger lgr = Logger.getLogger(Main.class.getName());
        try {
            if (this.con != null) {
                this.con.close();
            }
            if (this.rs != null) {
                this.rs.close();
            }
            if (this.st != null) {
                this.st.close();
            }
        } catch (SQLException ex) {
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}
