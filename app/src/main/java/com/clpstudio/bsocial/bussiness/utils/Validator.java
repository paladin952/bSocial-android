package com.clpstudio.bsocial.bussiness.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by clapalucian on 4/10/17.
 */

public final class Validator {

    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    public static boolean isUrl(String possibleUrl) {
        try {
            Pattern regex = Pattern.compile("\\b(?:(https?|ftp|file)://|www\\.)?[-A-Z0-9+&#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]\\.[-A-Z0-9+&@#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher regexMatcher = regex.matcher(possibleUrl);
            return regexMatcher.matches();
        } catch (PatternSyntaxException ignore) {
            // Syntax error in the regular expression
        }
        return false;
    }

}
