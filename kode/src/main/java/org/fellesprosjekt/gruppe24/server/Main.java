package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.Request;
import org.fellesprosjekt.gruppe24.common.models.User;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello, Server!");
        Server server = new Server() {
            protected Connection newConnection(){
                return new ServerConnection();
            }
        };
        server.start();

        KryoUtils.registerClasses(server.getKryo());

        try {
            System.out.println("Starting server on TCP port 9001 and UDP port 9002");
            server.bind(9001, 9002);
        } catch (IOException e) {
            e.printStackTrace();
            server.stop();
            return;
        }
        server.addListener(new Listener() {
            public void received(Connection connection, Object obj) {
                /*
                 * Her får vi tingen som ble sendt med client.sendTCP() i
                 * klienten. Vi sjekker typen dens med instanceof, og gjør
                 * noe basert på det. Dersom vi ikke kjenner igjen typen
                 * (dvs, vi har ikke sagt hva som skal skje), så printer vi
                 * ut det til stderr.
                 */
                ServerConnection conn = (ServerConnection) connection;

                Request req = (Request) obj;



                if (obj instanceof LoginInfo){
                    /* TODO: Hånter login-logikk
                     */
                    LoginInfo login = (LoginInfo) obj;
                    User user = new User();
                    user.setUsername(login.getUsername());
                    user.setPassword(login.getPassword());
                    conn.setUser(user);

                } else if (obj instanceof User) {

                } else if (obj instanceof String) {
                    String str = (String) obj;
                    System.out.println("User '" + conn.getUser().getUsername()+"' sier: ");
                    System.out.println(str);
                } else {
                    System.err.println("Ukjent datatype mottatt.");
                }
            }
        });
    }
}
