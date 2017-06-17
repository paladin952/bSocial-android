package com.clpstudio.database.models;

/**
 * Created by clapalucian on 03/06/2017.
 */

import java.util.ArrayList;
import java.util.List;

public class DbConversationModel {

    String id;
    String title;
    String imageUrl;
    List<String> membersIds = new ArrayList<>();
    List<DbRegisteredUserModel> users = new ArrayList<>();
    long timestamp;

    public DbConversationModel() {
    }

    public DbConversationModel(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public List<DbRegisteredUserModel> getUsers() {
        return users;
    }

    public void addUser(DbRegisteredUserModel model) {
        users.add(model);
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
