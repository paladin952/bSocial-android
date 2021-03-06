package com.clpstudio.bsocial.presentation.gifs;

import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.clpstudio.database.models.gifs.Data;
import com.clpstudio.domain.usecases.GifUseCases;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by clapalucian on 5/10/17.
 */

public class GifPresenter extends BaseMvpPresenter<GifPresenter.View> {

    @Inject
    GifUseCases gifUseCases;

    private Disposable disposable;

    @Inject
    public GifPresenter() {
    }

    public void getGifs(String text) {
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        disposable = gifUseCases.getGifs(text)
                .map(giphyResponse -> {
                    List<String> strings = new ArrayList<>();
                    for (Data data : giphyResponse.getData()) {
                        strings.add(data.getImages().getFixedHeight().getUrl());
                    }
                    return strings;
                })
                .subscribe(urls -> {
                    view().showGifs(urls);
                }, err -> {

                });

    }

    public void onGifSelected() {
        view().hideGifs();
    }

    public interface View extends IBaseMvpPresenter.View {

        void showGifs(List<String> urls);

        void hideGifs();

    }

}
