package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;

import org.fellesprosjekt.gruppe24.database.DatabaseManager;

import java.util.HashMap;

/**
 * Created by viktor on 26.02.15.
 */
public class DatabaseTest extends TestCase {
    public void setUp() {

    }

    public void testCanConnectToDefaultDB() {
        DatabaseManager dbm = new DatabaseManager();
        String expected = "abc";
        String actual= dbm.getCell("room_num", "Room");
        TestCase.assertEquals(expected, actual);
    }

    public void testCanGetRoom() {
        DatabaseManager dbm = new DatabaseManager();
        System.out.println(dbm.getRow("SELECT * FROM Room"));
        TestCase.assertNotNull(dbm.getRow("SELECT * FROM Room"));
    }

    public void testCanGetUser() {
        DatabaseManager dbm = new DatabaseManager();
        System.out.println(dbm.getRow("SELECT * FROM User"));
        TestCase.assertNotNull(dbm.getRow("SELECT * FROM User"));
    }

    public void testGetAutoIncrement() {
        int result = DatabaseManager.getAutoIncrement("Room");
        TestCase.assertTrue(result > -1);
    }

}
