package com.clpstudio.bsocial.data.models;

/**
 * Created by clapalucian on 4/10/17.
 */

public class RegisterModel {

    String username;
    String password;

    public RegisterModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
