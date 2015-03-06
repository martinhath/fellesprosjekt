package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;

import org.fellesprosjekt.gruppe24.database.DatabaseManager;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class DatabaseTest extends TestCase {
    public void setUp() {

    }

    public void testCanConnectToDefaultDB() {
        TestCase.assertNotNull(DatabaseManager.getStatement());
    }

}
