package com.clpstudio.bsocial.data.models.conversations;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

/**
 * Created by clapalucian on 5/10/17.
 */

@Parcel
public class ConversationViewModel {

    String id;
    String title;
    String imageUrl;
    List<String> membersIds;
    long timestamp;

    public ConversationViewModel() {
    }

    @ParcelConstructor
    public ConversationViewModel(String id, String title, String imageUrl) {
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
