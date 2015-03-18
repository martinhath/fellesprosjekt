package org.fellesprosjekt.gruppe24.common.models.net;


public class NotificationRequest extends Request{

    public boolean includeRead;
    public enum Handler {MEETING, GROUP, BOTH}

    public Handler handler;

    public NotificationRequest() {
        super();
        includeRead = false;
        handler = Handler.BOTH;
    }

    public NotificationRequest(Type t, Object o) {
        super(t,o);
        includeRead = false;
        handler = Handler.BOTH;
    }

    public NotificationRequest(Type t, boolean includeRead, Object o) {
        super(t,o);
        this.includeRead = includeRead;
        handler = Handler.BOTH;
    }
    public NotificationRequest(Type t, Handler handler , Object o) {
        super(t,o);
        this.includeRead = false;
        this.handler = handler;
    }
    public NotificationRequest(Type t, boolean includeRead, Handler handler , Object o) {
        super(t,o);
        this.includeRead = includeRead;
        this.handler = handler;
    }
}
