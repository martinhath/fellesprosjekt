package org.fellesprosjekt.gruppe24.common;

import com.esotericsoftware.kryo.Kryo;

import org.fellesprosjekt.gruppe24.common.models.*;
import org.fellesprosjekt.gruppe24.common.models.net.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class KryoUtils {
    /**
     * Kryonet må vite om alle klassene vi skal sende over nettverket.
     * Både server og klient må registrere alle klassene vi sender,
     * så vi bruker denne metoden til å skrive inn alle klassene vi
     * sender rundt, og så kaller vi
     *
     *     KryoUtils.registerClasses(server.getKryo());
     *
     * og
     *
     *     KryoUtils.registerClasses(client.getKryo());
     *
     * etter vi har laget server- og klientobjektene.
     * Obs! Dersom klassen har andre klasser som medlemsvariable,
     * f.eks. User som har List<Group> som medlem, så må også
     * den konkrete klassen (LinkedList) og Group også være med her.
     *
     * @param k En Kryo instans. Fåes f.eks. ved server.getKryo().
     */
    public static void registerClasses(Kryo k){
        // Models
        k.register(User.class);
        k.register(LinkedList.class);
        k.register(Group.class);
        k.register(LoginInfo.class);
        k.register(Calendar.class);
        k.register(Meeting.class);
        k.register(Room.class);
        k.register(LocalDateTime.class);

        // Java klasser
        k.register(ArrayList.class);

        // Requests
        k.register(Request.class);
        k.register(Request.Type.class);
        k.register(AuthRequest.class);
        k.register(AuthRequest.Action.class);
        k.register(InvitationRequest.class);
        k.register(InvitationRequest.Answer.class);
        k.register(UserRequest.class);

        // Response
        k.register(Response.class);
        k.register(Response.Type.class);
    }
}
