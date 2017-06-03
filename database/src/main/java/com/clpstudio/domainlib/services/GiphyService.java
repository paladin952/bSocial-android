package com.clpstudio.domainlib.services;


import com.clpstudio.domainlib.models.gifs.GiphyResponse;
import com.clpstudio.domainlib.retrofit.RetrofitGiphyApiService;

import javax.inject.Inject;

import io.reactivex.Single;


public class GiphyService {

    private static final String API_KEY = "dc6zaTOxFJmzC";

    @Inject
    RetrofitGiphyApiService retrofitGiphyApiService;

    @Inject
    public GiphyService() {

    }

    public Single<GiphyResponse> getGifs(String gifs) {
        return retrofitGiphyApiService.getGifs(gifs, API_KEY);
    }
}
