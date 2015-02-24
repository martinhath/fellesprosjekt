package org.fellesprosjekt.gruppe24.common.models;

import com.esotericsoftware.kryo.Kryo;

public class KryoUtils {
    public static void registerClasses(Kryo k){
        k.register(User.class);
    }
}
