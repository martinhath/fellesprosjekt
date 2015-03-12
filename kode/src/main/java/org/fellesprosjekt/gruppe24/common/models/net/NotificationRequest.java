package org.fellesprosjekt.gruppe24.common.models.net;

public class NotificationRequest extends Request{

    public boolean includeRead;

    public NotificationRequest() {
        super();
        includeRead = false;
    }

    public NotificationRequest(Type t, Object o) {
        super(t,o);
        includeRead = false;
    }

    public NotificationRequest(Type t, boolean includeRead, Object o) {
        super(t,o);
        this.includeRead = includeRead;
    }
}
