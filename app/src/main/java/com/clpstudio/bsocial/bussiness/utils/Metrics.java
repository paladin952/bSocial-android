package com.clpstudio.bsocial.bussiness.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by clapalucian on 5/6/17.
 */

public class Metrics {

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
