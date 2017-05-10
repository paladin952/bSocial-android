package com.clpstudio.bsocial.data.models.conversations;

/**
 * Created by clapalucian on 5/10/17.
 */

public class ConversationNameModel {

    private String name;
    private String imageUrl;

    public ConversationNameModel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
