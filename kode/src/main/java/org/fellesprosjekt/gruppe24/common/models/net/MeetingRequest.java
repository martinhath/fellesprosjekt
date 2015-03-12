package org.fellesprosjekt.gruppe24.common.models.net;

public class MeetingRequest extends Request {
    public enum Handler {USER, GROUP, NA} // to decide whether list requests should act on user or group

    public Handler handler;

    public MeetingRequest(Type type, Object payload) {
        super(type, payload);
        this.handler = Handler.NA;
    }
    public MeetingRequest(Type type, Handler handler, Object payload) {
        super(type, payload);
        this.handler = handler;
    }
}
