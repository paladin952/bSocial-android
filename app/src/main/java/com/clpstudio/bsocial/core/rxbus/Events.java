package com.clpstudio.bsocial.core.rxbus;

import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;

/**
 * Created by clapalucian on 05/07/2017.
 */

public abstract class Events {

    public class MessageEvent {

        private MessageViewModel model;

        public MessageEvent(MessageViewModel messageViewModel) {
            this.model = messageViewModel;
        }

        public MessageViewModel getModel() {
            return model;
        }
    }

}
