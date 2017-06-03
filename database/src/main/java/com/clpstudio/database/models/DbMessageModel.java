package com.clpstudio.database.models;

/**
 * Created by clapalucian on 5/6/17.
 */

public class DbMessageModel {

    public static int TYPE_MESSAGE = 0;
    public static int TYPE_GIF = 1;
    public static int TYPE_PHOTO = 2;
    public static int TYPE_AUDIO = 3;
    public static int TYPE_VIDEO = 4;
    public static int TYPE_LINK = 5;

    String userName;
    String message;
    long timestamp;
    String imageUrl;
    int type;

    public DbMessageModel() {
    }

    public DbMessageModel(String userName, String message, long timestamp, String imageUrl, int type) {
        this.userName = userName;
        this.message = message;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DbMessageModel{" +
                "userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
