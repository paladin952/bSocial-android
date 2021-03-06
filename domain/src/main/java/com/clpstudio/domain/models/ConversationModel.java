package com.clpstudio.domain.models;

/**
 * Created by clapalucian on 03/06/2017.
 */

import java.util.List;

public class ConversationModel {

    String id;
    String title;
    String imageUrl;
    List<String> membersIds;
    long timestamp;

    public ConversationModel() {
    }

    public ConversationModel(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public List<String> getMembersIds() {
        return membersIds;
    }

    public void setMembersIds(List<String> data) {
        this.membersIds = data;
    }

}
