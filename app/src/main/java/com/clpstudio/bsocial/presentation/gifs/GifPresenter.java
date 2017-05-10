package com.clpstudio.bsocial.presentation.gifs;

import com.clpstudio.bsocial.bussiness.service.GiphyService;
import com.clpstudio.bsocial.data.models.gifs.Data;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by clapalucian on 5/10/17.
 */

public class GifPresenter extends BaseMvpPresenter<GifPresenter.View> {

    @Inject
    GiphyService giphyService;

    @Inject
    public GifPresenter() {
    }

    public void getGifs(String text) {
        giphyService.getGifs(text)
                .map(giphyResponse -> {
                    List<String> strings = new ArrayList<String>();
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

    public interface View extends IBaseMvpPresenter.View {

        void showGifs(List<String> urls);

    }

}
