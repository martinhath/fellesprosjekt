package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

public class ClientListener extends Listener{

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
}
