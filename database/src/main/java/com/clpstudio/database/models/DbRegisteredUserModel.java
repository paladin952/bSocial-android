package com.clpstudio.database.models;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class DbRegisteredUserModel {

    String userId;
    String email;
    String imageUrl;

    public DbRegisteredUserModel() {

    }

    public DbRegisteredUserModel(String userId, String email, String imageUrl) {
        this.email = email;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "DbRegisteredUserModel{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
