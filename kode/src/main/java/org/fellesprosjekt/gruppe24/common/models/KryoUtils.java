package org.fellesprosjekt.gruppe24.common.models;

import com.esotericsoftware.kryo.Kryo;

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
     *
     * @param k En Kryo instans. Fåes f.eks. ved server.getKryo().
     */
    public static void registerClasses(Kryo k){
        k.register(User.class);
    }
}
