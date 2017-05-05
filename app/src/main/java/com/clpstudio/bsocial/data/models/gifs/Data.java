package com.clpstudio.bsocial.data.models.gifs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by clapalucian on 5/5/17.
 */

public class Data {

    @SerializedName("images") @Expose
    private Images images;

    public Data() {
    }

    public Data(final Images images) {
        this.images = images;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(final Images images) {
        this.images = images;
    }

}
