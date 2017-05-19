package com.clpstudio.bsocial.data.models.conversations;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by clapalucian on 5/10/17.
 */

@Parcel
public class ConversationModel {

    String id;
    String title;
    String imageUrl;

    public ConversationModel() {
    }

    @ParcelConstructor
    public ConversationModel(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
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
}
