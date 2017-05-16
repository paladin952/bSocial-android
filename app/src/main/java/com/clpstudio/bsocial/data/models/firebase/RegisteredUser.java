package com.clpstudio.bsocial.data.models.firebase;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class RegisteredUser {

    private String email;

    public RegisteredUser() {
    }

    public RegisteredUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "email='" + email + '\'' +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
