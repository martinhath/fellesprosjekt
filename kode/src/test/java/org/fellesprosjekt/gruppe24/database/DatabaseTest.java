package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;

import org.junit.Test;


public class DatabaseTest {

    @Test
    public void testCanConnectToDefaultDB() {
        TestCase.assertNotNull(DatabaseManager.getStatement());
    }

}
