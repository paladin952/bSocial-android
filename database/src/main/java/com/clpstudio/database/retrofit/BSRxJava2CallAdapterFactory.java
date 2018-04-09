package com.clpstudio.database.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class BSRxJava2CallAdapterFactory extends CallAdapter.Factory {
    private final CallAdapter.Factory delegate;

    public static BSRxJava2CallAdapterFactory create(CallAdapter.Factory delegate) {
        return new BSRxJava2CallAdapterFactory(delegate);
    }

    private BSRxJava2CallAdapterFactory(CallAdapter.Factory delegate) {
        this.delegate = delegate;
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType != Completable.class && rawType != Flowable.class && rawType != Single.class
                && rawType != Maybe.class && rawType != Observable.class) {
            return null;
        }
        return new BSRxJava2CallAdapter(delegate.get(returnType, annotations, retrofit));
    }
}