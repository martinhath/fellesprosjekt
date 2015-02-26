package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Request.Type;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Client!");
        Client client = new Client();
        client.start();

        KryoUtils.registerClasses(client.getKryo());

        try{
            client.connect(5000, "127.0.0.1", 9001, 9002);
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
        client.addListener(new Listener() {
            @Override
            public void received(Connection conn, Object obj) {
                if (obj instanceof Response) {
                    Response res = (Response) obj;
                    if (res.getType() == Response.Type.FAILURE){
                        System.err.println("Failure: " + res.getPayload());
                        return;
                    }
                    Class t = res.getModel();

                    if (t == User.class){
                        User user = (User) res.getPayload();
                        System.out.println("User: " +user.getUsername());
                    } else {
                        System.out.println(res.getType());
                        System.out.println(res.getPayload());
                    }

                }
                else{
                    System.out.println("default");
                    System.out.println(obj.toString());
                }
            }
        });

        LoginInfo login = new LoginInfo("martin", "passord123");

        Request req = new Request(Type.PUT, User.class);
        req.setPayload(login);

        client.sendTCP(req);

        req = new Request(Type.GET, User.class);
        req.setPayload(null);
        client.sendTCP(req);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.close();
    }

}
