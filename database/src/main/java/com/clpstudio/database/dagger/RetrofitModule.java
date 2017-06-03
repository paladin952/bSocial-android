package com.clpstudio.database.dagger;

import android.content.Context;

import com.clpstudio.database.retrofit.BSRxJava2CallAdapterFactory;
import com.clpstudio.database.retrofit.RetrofitGiphyApiService;
import com.clpstudio.database.retrofit.StringConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    private static final String PROTOCOL = "https://";
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final String GIPHY_ENDPOINT = "api.giphy.com";

    @Provides
    @Singleton
    public RetrofitGiphyApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(RetrofitGiphyApiService.class);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Retrofit.Builder builder, OkHttpClient okHttpClient) {
        return builder.client(okHttpClient).build();
    }

    @Provides
    @Singleton
    public Retrofit.Builder provideRetrofitGiphyBuilder(Gson gson, Context context) {
        return new Retrofit.Builder()
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(BSRxJava2CallAdapterFactory.create(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())))
                .baseUrl(PROTOCOL + GIPHY_ENDPOINT);
    }
}

