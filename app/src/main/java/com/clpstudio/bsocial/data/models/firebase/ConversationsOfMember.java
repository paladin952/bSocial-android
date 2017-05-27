package com.clpstudio.bsocial.data.models.firebase;

/**
 * Created by clapalucian on 27/05/2017.
 */

public class ConversationsOfMember {

    boolean isVisible;
    String conversationId;

    public ConversationsOfMember() {
    }

    public ConversationsOfMember(boolean isVisible, String conversationId) {
        this.isVisible = isVisible;
        this.conversationId = conversationId;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public String getConversationId() {
        return conversationId;
    }
}
