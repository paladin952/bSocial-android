package com.clpstudio.bsocial.data.models;

import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;
import com.clpstudio.database.models.DbConversationModel;
import com.clpstudio.database.models.DbRegisteredUserModel;
import com.clpstudio.domain.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clapalucian on 03/06/2017.
 */

public class Mapper {

    public static ConversationViewModel toConversationViewModel(DbConversationModel model) {
        return new ConversationViewModel(model.getId(), model.getTitle(), model.getImageUrl(), model.getMembersIds(), toRegisteredUserViewModels(model.getUsers()));
    }

    public static List<ConversationViewModel> toConversationViewModels(List<DbConversationModel> data) {
        List<ConversationViewModel> result = new ArrayList<>();
        for (DbConversationModel model : data) {
            result.add(toConversationViewModel(model));
        }
        return result;
    }


    public static MessageViewModel toMessageViewModel(Message message) {
        return new MessageViewModel(message.getUserName(), message.getMessage(), message.getTimestamp(), message.getImageUrl(), message.getType(), message.getConversationId());
    }

    public static List<MessageViewModel> toMessageViewModels(List<Message> data) {
        List<MessageViewModel> result = new ArrayList<>();
        for (Message message : data) {
            result.add(toMessageViewModel(message));
        }

        return result;
    }

    public static DbRegisteredUserModel toRegisteredUser(RegisteredUserViewModel model) {
        return new DbRegisteredUserModel(model.getUserId(), model.getEmail(), model.getImageUrl());
    }

    public static RegisteredUserViewModel toRegisteredUserViewModel(DbRegisteredUserModel model) {
        return new RegisteredUserViewModel(model.getUserId(), model.getEmail(), model.getImageUrl());
    }

    public static List<RegisteredUserViewModel> toRegisteredUserViewModels(List<DbRegisteredUserModel> data) {
        List<RegisteredUserViewModel> result = new ArrayList<>();

        for (DbRegisteredUserModel model : data) {
            result.add(toRegisteredUserViewModel(model));
        }

        return result;
    }
}
