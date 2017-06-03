package com.clpstudio.domainlib.models.gifs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by clapalucian on 5/5/17.
 */

public class Images {

    @SerializedName("fixed_height") @Expose
    private FixedHeight fixedHeight;

    public Images() {
    }

    public Images(final FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public FixedHeight getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(final FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
    }
}

