package com.clpstudio.domain.models;

import com.clpstudio.database.models.DbConversationModel;
import com.clpstudio.database.models.DbMessageModel;
import com.clpstudio.database.models.DbRegisteredUserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clapalucian on 03/06/2017.
 */

public class Mapper {

    public static ConversationModel toConversationModel(DbConversationModel model) {
        return new ConversationModel(model.getId(), model.getTitle(), model.getImageUrl());
    }

    public static List<ConversationModel> toConversationModels(List<DbConversationModel> data) {
        List<ConversationModel> result = new ArrayList<>();
        for (DbConversationModel model : data) {
            result.add(toConversationModel(model));
        }
        return result;
    }


    public static Message toMessage(DbMessageModel message) {
        return new Message(message.getUserName(), message.getMessage(), message.getTimestamp(), message.getImageUrl(), message.getType());
    }

    public static List<Message> toMessages(List<DbMessageModel> data) {
        List<Message> result = new ArrayList<>();
        for (DbMessageModel message : data) {
            result.add(toMessage(message));
        }

        return result;
    }

    public static RegisteredUser toRegisteredUser(DbRegisteredUserModel model) {
        return new RegisteredUser(model.getUserId(), model.getEmail(), model.getImageUrl());
    }

    public static List<RegisteredUser> toRegisteredUserViewModels(List<DbRegisteredUserModel> data) {
        List<RegisteredUser> result = new ArrayList<>();

        for (DbRegisteredUserModel model : data) {
            result.add(toRegisteredUser(model));
        }

        return result;
    }

}
