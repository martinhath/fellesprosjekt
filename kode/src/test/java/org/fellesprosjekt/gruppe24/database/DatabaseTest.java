package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;

import org.fellesprosjekt.gruppe24.database.DatabaseManager;

import javax.xml.crypto.Data;
import java.util.HashMap;

/**
 * Created by viktor on 26.02.15.
 */
public class DatabaseTest extends TestCase {
    public void setUp() {

    }

    public void testCanConnectToDefaultDB() {
        TestCase.assertNotNull(DatabaseManager.getStatement());
    }


    public void testGetAutoIncrement() {
        int result = DatabaseManager.getAutoIncrement("Room");
        TestCase.assertTrue(result > -1);
    }

}
