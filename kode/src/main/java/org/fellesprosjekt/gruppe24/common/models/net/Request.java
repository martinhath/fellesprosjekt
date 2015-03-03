package org.fellesprosjekt.gruppe24.common.models.net;

public abstract class Request {
    public enum Type {POST, PUT, GET, LIST}

    public Type type;
    public Object payload;

    public Request() {}

    public Request(Type type, Object payload){
        this.type = type;
        this.payload = payload;
    }

}
