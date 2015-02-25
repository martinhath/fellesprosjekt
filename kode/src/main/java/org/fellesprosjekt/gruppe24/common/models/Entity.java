package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public abstract class Entity {

    private String name;

    /**
     * Gruppene denne entiteten er medlem av
     * OBS! For at det skal være mulig å sende Entity
     * over kryonet, må groups feltet være transient.
     * Dette betyr i praksis at groups alltid vil være null
     * etter ett entityobjekt er sendt over nettverket.
     *
     * TODO: fiks dette; vi må legge inn alle gruppene hver
     * entity er med i etter det er sendt over nettverk.
     */
    protected transient List<Group> groups;


    public Entity(){
        groups = new LinkedList<>();
    }

    public Entity(String name){
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
