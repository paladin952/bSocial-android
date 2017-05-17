package com.clpstudio.bsocial.data.models.firebase;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class RegisteredUser {

    private String userId;
    private String email;

    public RegisteredUser() {
    }

    public RegisteredUser(String userId, String email) {
        this.email = email;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
