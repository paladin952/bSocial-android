package com.clpstudio.bsocial.bussiness.utils;

/**
 * Created by clapalucian on 5/10/17.
 */

public class MessageTypedResolver {

    public static final int TYPE_NORMAL_MESSAGE = 0;
    public static final int TYPE_GIF_MESSAGE = 1;

    public static int getMessageType(String message) {
        if (message.startsWith("https://media0.giphy.com")) {
            return TYPE_GIF_MESSAGE;
        }
        return TYPE_NORMAL_MESSAGE;
    }

}
