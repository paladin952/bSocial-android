package com.clpstudio.domain.usecases;

import com.clpstudio.database.models.gifs.GiphyResponse;
import com.clpstudio.database.services.GiphyService;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Created by clapalucian on 04/06/2017.
 */

public class GifUseCases {

    @Inject
    GiphyService giphyService;

    @Inject
    public GifUseCases() {
    }

    public Single<GiphyResponse> getGifs(String gifs) {
        return giphyService.getGifs(gifs);
    }

}
