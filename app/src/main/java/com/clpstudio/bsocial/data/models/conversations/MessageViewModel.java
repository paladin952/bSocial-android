package com.clpstudio.bsocial.data.models.conversations;

/**
 * Created by clapalucian on 5/6/17.
 */

public class MessageViewModel {

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
    String conversationId;

    public MessageViewModel() {
    }

    public MessageViewModel(String userName, String message, long timestamp, String imageUrl, int type, String conversationId) {
        this.userName = userName;
        this.message = message;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.type = type;
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        return conversationId;
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
        return "MessageViewModel{" +
                "userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
