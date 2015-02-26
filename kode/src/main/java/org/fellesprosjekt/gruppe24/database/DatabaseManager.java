package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.server.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {

    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    
    Logger lgr;

    private String url;
    private String user;
    private String password;

    /**
     * Initializes a specific database
     *
     * @param url url to the database
     * @param user username in the database
     * @param password user's password to the database
     */
    public DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        lgr = Logger.getLogger(Main.class.getName());
    }

    /**
     * Initializes a default database
     *
     */
    public DatabaseManager() {
        this.url = "jdbc:mysql://mysql.stud.ntnu.no:3306/hermanmk_calDB";
        this.user = "hermanmk_cal";
        this.password = "cal123";
        lgr = Logger.getLogger(Main.class.getName());
    }
    
    public void createConnection() {
    	try{
    		con = DriverManager.getConnection(url, user, password);
    		lgr.log(Level.INFO, con.toString());
    	 } catch (SQLException ex) {
             lgr.log(Level.SEVERE, ex.getMessage(), ex);
         }
    }
    
    public Statement getStatement() {
    	try {
    		st = con.createStatement();
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
     * @param from which table to select from
     * @return a string representing the value of desired cell
     */
    public String getCell(String select, String from) {
        try {
            String query = String.format("SELECT * FROM %s;", from);
            createConnection();
            st = getStatement();
            lgr.log(Level.INFO, "Executing query: " + query);
            rs = st.executeQuery(query);

            if (rs.next()) {
                return rs.getString(select);
            }

        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            close();
        }
        return null;
    }

    /**
     * Makes sure the connection to the database is closed
     */
    public void close() {
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
        	lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
	 * SQL Query that do not alter the database. Eg. SELECT queries
	 * 
	 * @param sql
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet readQuery(String query) {
		try {
			st = getStatement();
			lgr.log(Level.INFO, "Executing query: " + query);
			rs = st.executeQuery(query);
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
	 * @param sql
	 * @throws SQLException
	 */
	public void updateQuery(String query) {
		try {
			st = getStatement();
			lgr.log(Level.INFO, "Executing query: " + query);
			st.executeUpdate(query);
		} catch (SQLException ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}

	}
    
    /**
	 * Executes a SQL query, and returns a HashMap with the columns specified in the query.
	 * Should also work when using the 'AS' keyword because it gets the labels and not names.
	 * @param query
	 * @return a HashMap<String, String> with the column labels as keys.
	 */
	public HashMap<String, String> getRow(String query) {
		HashMap<String, String> result = new HashMap<String, String>();
		createConnection();
		rs = readQuery(query);
		try {
			if(rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for(int i = 1; i <= rsmd.getColumnCount(); i++) {
					result.put(rsmd.getColumnLabel(i), rs.getString(i));
				}
			}
		} catch (SQLException ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return result;
	}
	
	/**
	 * Executes a SQL query, and returns an ArrayList with all the resulting rows.
	 * Useful when querying for a list of objects.
	 * Should also work when using the 'AS' keyword because it gets the labels and not names.
	 * @param query
	 * @return an ArrayList containing all the rows as nested HashMap<String, String>
	 */
	protected ArrayList<HashMap<String, String>> getList(String query) {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		createConnection();
		rs = readQuery(query);
		try {
			while(rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				HashMap<String, String> row = new HashMap<String, String>();
				for(int i = 1; i <= rsmd.getColumnCount(); i++) {
					row.put(rsmd.getColumnLabel(i), rs.getString(i));
				}
				result.add(row);
			}
		} catch (SQLException ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return result;
	}
}
