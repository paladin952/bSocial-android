package com.clpstudio.bsocial.core.retrofit;


import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;
import retrofit2.CallAdapter;

public class BSRxJava2CallAdapter implements CallAdapter<Object> {
    private static final String TAG = "RxErrors";

    private final CallAdapter<?> delegate;

    public BSRxJava2CallAdapter(CallAdapter<?> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Type responseType() {
        return delegate.responseType();
    }

    @Override
    public <R> Object adapt(Call<R> call) {
        Object result = delegate.adapt(call);
        if (result instanceof Completable) {
            return ((Completable) result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(throwable -> Completable.error(wrapThrowable(throwable)));
        }
        if (result instanceof Flowable) {
            return ((Flowable<?>) result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext((Throwable throwable) -> Flowable.error(wrapThrowable(throwable)));
        }
        if (result instanceof Single) {
            return ((Single<?>) result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(throwable -> Single.error(wrapThrowable(throwable)));
        }
        if (result instanceof Maybe) {
            return ((Maybe<?>) result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext((Throwable throwable) -> Maybe.error(wrapThrowable(throwable)));
        }
        if (result instanceof Observable) {
            return ((Observable<?>) result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext((Throwable throwable) -> Observable.error(wrapThrowable(throwable)));
        }
        throw new RuntimeException("Unsupported call type");
    }

    private Throwable wrapThrowable(Throwable throwable) {
        return throwable;
    }
}

