package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;

import org.junit.Test;

import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseTest {

    @Test
    public void testCanConnectToDefaultDB() {
        Statement s = DatabaseManager.getStatement();
        TestCase.assertNotNull(s);
        try {
            s.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
