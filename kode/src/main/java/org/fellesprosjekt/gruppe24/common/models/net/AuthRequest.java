package org.fellesprosjekt.gruppe24.common.models.net;

import org.fellesprosjekt.gruppe24.common.models.LoginInfo;

public class AuthRequest extends Request {

    public enum Action {LOGIN, LOGOUT}

    public AuthRequest() {}

    public AuthRequest(Type type, Action action, LoginInfo loginInfo) {
        this.type = type;
        this.payload = loginInfo;
    }
}
