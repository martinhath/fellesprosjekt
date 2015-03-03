package org.fellesprosjekt.gruppe24.common.models.net;

import org.fellesprosjekt.gruppe24.common.models.LoginInfo;

public class LoginRequest extends Request {

    public LoginRequest() {}

    public LoginRequest(Type type, LoginInfo loginInfo) {
        this.type = type;
        this.payload = loginInfo;
    }
}
