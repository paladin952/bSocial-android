package com.clpstudio.bsocial.data.models.firebase;

/**
 * Created by clapalucian on 19/05/2017.
 */

public class Conversation {

    private String id;
    private String title;

    public Conversation(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
