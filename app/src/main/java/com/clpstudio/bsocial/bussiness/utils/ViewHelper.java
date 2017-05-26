package com.clpstudio.bsocial.bussiness.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.clpstudio.bsocial.core.listeners.ClickListener;

/**
 * Created by clapalucian on 27/05/2017.
 */

public class ViewHelper {

    public static void applyLink(TextView textView, final String url, ClickListener<String> clickListener) {
        SpannableString ss = new SpannableString(url);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                clickListener.click(url);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        textView.setHighlightColor(Color.TRANSPARENT);
    }

    public static void removeLink(TextView textView) {
        textView.setMovementMethod(null);
    }

}
