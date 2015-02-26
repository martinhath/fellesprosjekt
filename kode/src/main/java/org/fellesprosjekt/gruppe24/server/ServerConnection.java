package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import org.fellesprosjekt.gruppe24.common.models.User;

public class ServerConnection extends Connection {
    private User user;

    public User getUser(){
        return user;
    }

    public void setUser(User u){
        user = u;
    }

}
