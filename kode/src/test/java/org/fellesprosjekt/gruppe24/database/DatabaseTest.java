package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;

import org.fellesprosjekt.gruppe24.TestInitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.sql.Statement;


@RunWith(TestInitRunner.class)
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
