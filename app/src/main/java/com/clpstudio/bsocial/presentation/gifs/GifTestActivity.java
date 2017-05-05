package com.clpstudio.bsocial.presentation.gifs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.service.GiphyService;
import com.clpstudio.bsocial.data.models.gifs.Data;
import com.clpstudio.bsocial.presentation.BSocialApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GifTestActivity extends AppCompatActivity {

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, GifTestActivity.class));
    }

    @Inject
    GiphyService giphyService;

    @BindView(R.id.horizontalView)
    GifHorizontalListView horizontalListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_test);
        ButterKnife.bind(this);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);
        horizontalListView.setClickListener(element -> {
            Log.d("luci", element);
        });
        giphyService.getGifs("cat")
                .map(giphyResponse -> {
                    List<String> strings = new ArrayList<String>();
                    for (Data data : giphyResponse.getData()) {
                        strings.add(data.getImages().getFixedHeight().getUrl());
                    }
                    return strings;
                })
                .subscribe(urls -> {
                    horizontalListView.addData(urls);
                }, err -> {

                });

    }
}
