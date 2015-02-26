package org.fellesprosjekt.gruppe24.common.models;

public class Request {
    public enum Type {GET, LIST, PUT};

    private Type type;

    private Class model;

    private Object payload;


    public Request() {}

    public Request(Type t, Class c){
        this(t, c, null);
    }

    public Request(Type t, Class c, Object payload){
        type = t;
        model = c;
        this.payload = payload;
    }

    public Type getType(){
        return type;
    }

    public Class getModel(){
        return model;
    }

    public void setPayload(Object payload){
        this.payload = payload;
    }

    public Object getPayload(){
        return payload;
    }

}
