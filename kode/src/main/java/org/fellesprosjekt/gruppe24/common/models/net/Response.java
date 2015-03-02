package org.fellesprosjekt.gruppe24.common.models.net;

public class Response {
    public enum Type {SUCCESS, FAILURE}

    private Type type;

    private Class model;

    private Object payload;


    public Response() {}

    public Response(Type t, Class c){
        this(t, c, null);
    }

    public Response(Type t, Class c, Object payload){
        type = t;
        model = c;
        this.payload = payload;
    }

    public void setType(Type t){
        type = t;
    }

    public Type getType(){
        return type;
    }

    public void setModel(Class c){
        model = c;
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
