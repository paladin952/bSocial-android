package com.clpstudio.domain.models.gifs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clapalucian on 5/5/17.
 */

public class GiphyResponse {

    @SerializedName("data") @Expose
    private List<Data> data = new ArrayList<>();
    public GiphyResponse() {
    }
    public GiphyResponse(final List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(final List<Data> data) {
        this.data = data;
    }
}
