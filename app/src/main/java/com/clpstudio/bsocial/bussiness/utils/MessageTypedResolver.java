package com.clpstudio.bsocial.bussiness.utils;

import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by clapalucian on 5/10/17.
 */

public class MessageTypedResolver {

    public static boolean isGifMessage(String url) {
        if (TextUtils.isEmpty(url)){
            return false;
        }
        Uri uri = Uri.parse(url);
        String host = uri.getHost();
        if (host != null) {
            return host.contains("giphy.com") && url.contains(".gif");
        } else {
            return false;
        }
    }
}
