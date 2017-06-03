package com.clpstudio.database.services;


import com.clpstudio.database.models.gifs.GiphyResponse;
import com.clpstudio.database.retrofit.RetrofitGiphyApiService;

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
