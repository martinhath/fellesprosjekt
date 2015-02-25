package org.fellesprosjekt.gruppe24.common.models;

public abstract class Entity {
    private String name;

    protected void setName(String Name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
