package com.clpstudio.bsocial.data.models.conversations;

/**
 * Created by clapalucian on 5/6/17.
 */

public class Message {

    String userName;
    String message;
    long timestamp;
    String imageUrl;

    public Message() {
    }

    public Message(String userName, String message, long timestamp, String imageUrl) {
        this.userName = userName;
        this.message = message;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
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
        return "Message{" +
                "userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
