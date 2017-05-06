package com.clpstudio.bsocial.data.models.conversations;

/**
 * Created by clapalucian on 5/6/17.
 */

public class ConversationModel {

    private String userName;
    private String message;

    public ConversationModel(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ConversationModel{" +
                "message='" + message + '\'' +
                '}';
    }
}
