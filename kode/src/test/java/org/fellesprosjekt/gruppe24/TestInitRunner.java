package org.fellesprosjekt.gruppe24;

import org.fellesprosjekt.gruppe24.database.DatabaseManager;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class TestInitRunner extends BlockJUnit4ClassRunner{
    public TestInitRunner(Class<?> klass) throws InitializationError {
        super(klass);
        DatabaseManager.init(DatabaseManager.Type.TEST);
    }
}
