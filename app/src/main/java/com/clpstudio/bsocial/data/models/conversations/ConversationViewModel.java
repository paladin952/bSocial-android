package com.clpstudio.bsocial.data.models.conversations;

import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clapalucian on 5/10/17.
 */

@Parcel
public class ConversationViewModel {

    String id;
    String title;
    String imageUrl;
    List<String> membersIds = new ArrayList<>();
    long timestamp;
    List<RegisteredUserViewModel> userViewModels;

    public ConversationViewModel() {
    }

    @ParcelConstructor
    public ConversationViewModel(String id, String title, String imageUrl, List<String> membersIds, List<RegisteredUserViewModel> userViewModels) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.userViewModels = userViewModels;
        this.membersIds = membersIds;
    }

    public List<RegisteredUserViewModel> getUserViewModels() {
        return userViewModels;
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

    public void setId(String id) {
        this.id = id;
    }
}
