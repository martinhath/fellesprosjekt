package org.fellesprosjekt.gruppe24;

import com.esotericsoftware.kryonet.Client;
import org.fellesprosjekt.gruppe24.common.Kryo.KryoUtils;
import org.fellesprosjekt.gruppe24.database.DatabaseManager;
import org.fellesprosjekt.gruppe24.server.CalendarServer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.io.IOException;

public class TestInitRunner extends BlockJUnit4ClassRunner{
    public static Client client;
    public static CalendarServer server;

    public TestInitRunner(Class<?> klass) throws InitializationError, IOException {
        super(klass);
        DatabaseManager.init(DatabaseManager.Type.TEST);

        int tcp = 6788;
        int udp = 6789;
        if (server == null) {
            server = new CalendarServer(tcp, udp);
            KryoUtils.registerClasses(server.getServer().getKryo());
            server.start();
        }
        if (client == null) {
            client = new Client();
            client.start();
            client.connect(5000, "127.0.0.1", tcp, udp);
            KryoUtils.registerClasses(client.getKryo());
        }
    }
}
