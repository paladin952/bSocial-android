package com.clpstudio.bsocial.bussiness.api;

import com.clpstudio.bsocial.data.models.gifs.GiphyResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by clapalucian on 4/10/17.
 */

public interface RetrofitGiphyApiService {

    @GET("/v1/gifs/search")
    Single<GiphyResponse> getGifs(@Query("q") String searchText, @Query("api_key") String key);

    @GET("/v1/gifs/search")
    Observable<GiphyResponse> getSearchResults(@Query("q") final String searchString,
                                               @Query("limit") final int limit,
                                               @Query("api_key") final String apiKey);

}
