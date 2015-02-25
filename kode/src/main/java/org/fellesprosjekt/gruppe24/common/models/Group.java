package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public class Group extends Entity{
    private List<Entity> members;

    @SuppressWarnings("unused")
    public Group(){}

    public Group(String name) {
        setName(name);
        members = new LinkedList<>();
    }
}
