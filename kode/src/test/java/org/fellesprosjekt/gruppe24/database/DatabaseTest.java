package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;

import org.fellesprosjekt.gruppe24.database.DatabaseManager;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class DatabaseTest {
    @Test
    public void testCanConnectToDefaultDB() {
        TestCase.assertNotNull(DatabaseManager.getStatement());
    }

}
