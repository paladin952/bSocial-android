package com.clpstudio.bsocial.data.models;

import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;
import com.clpstudio.domainlib.models.ConversationModel;
import com.clpstudio.domainlib.models.Message;
import com.clpstudio.domainlib.models.RegisteredUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clapalucian on 03/06/2017.
 */

public class Mapper {

    public static ConversationViewModel toConversationViewModel(ConversationModel model) {
        return new ConversationViewModel(model.getId(), model.getTitle(), model.getImageUrl());
    }

    public static List<ConversationViewModel> toConversationViewModels(List<ConversationModel> data) {
        List<ConversationViewModel> result = new ArrayList<>();
        for (ConversationModel model : data) {
            result.add(toConversationViewModel(model));
        }
        return result;
    }


    public static MessageViewModel toMessageViewModel(Message message) {
        return new MessageViewModel(message.getUserName(), message.getMessage(), message.getTimestamp(), message.getImageUrl(), message.getType());
    }

    public static List<MessageViewModel> toMessageViewModels(List<Message> data) {
        List<MessageViewModel> result = new ArrayList<>();
        for (Message message : data) {
            result.add(toMessageViewModel(message));
        }

        return result;
    }

    public static RegisteredUser toRegisteredUser(RegisteredUserViewModel model) {
        return new RegisteredUser(model.getUserId(), model.getEmail(), model.getImageUrl());
    }

    public static RegisteredUserViewModel toRegisteredUserViewModel(RegisteredUser model) {
        return new RegisteredUserViewModel(model.getUserId(), model.getEmail(), model.getImageUrl());
    }

    public static List<RegisteredUserViewModel> toRegisteredUserViewModels(List<RegisteredUser> data) {
        List<RegisteredUserViewModel> result = new ArrayList<>();

        for (RegisteredUser model : data) {
            result.add(toRegisteredUserViewModel(model));
        }

        return result;
    }
}
