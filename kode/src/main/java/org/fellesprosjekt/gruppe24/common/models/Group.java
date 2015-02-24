package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public class Group {
    private String name;
    private List<User> members;

    @SuppressWarnings("unused")
    public Group(){}

    public Group(String name) {
        this.name = name;
        members = new LinkedList<>();
    }
}
