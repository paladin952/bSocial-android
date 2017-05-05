package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.bussiness.api.RetrofitGiphyApiService;
import com.clpstudio.bsocial.data.models.gifs.GiphyResponse;

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
