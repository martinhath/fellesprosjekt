package org.fellesprosjekt.gruppe24.common.models.net;

public class Response {
    public enum Type {OK, FAIL}

    public Type type;
    public Object payload;

    public Response() {}

    public Response(Type type, Object payload){
        this.type = type;
        this.payload = payload;
    }

    public static Response GetFailResponse(String s){
        return new Response(Type.FAIL, s);
    }

}
