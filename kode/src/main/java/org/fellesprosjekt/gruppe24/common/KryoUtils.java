package org.fellesprosjekt.gruppe24.common;

import com.esotericsoftware.kryo.Kryo;
import org.fellesprosjekt.gruppe24.common.models.*;

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
        k.register(User.class);
        k.register(LinkedList.class);
        k.register(Group.class);
        k.register(LoginInfo.class);

        k.register(Request.class);
        k.register(Meeting.class);
    }
}
