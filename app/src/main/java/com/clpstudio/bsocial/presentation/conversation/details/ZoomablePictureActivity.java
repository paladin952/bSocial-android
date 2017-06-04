package com.clpstudio.bsocial.presentation.conversation.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clpstudio.bsocial.Henson;
import com.clpstudio.bsocial.R;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 04/06/2017.
 */

public class ZoomablePictureActivity extends AppCompatActivity {

    @BindView(R.id.photo)
    PhotoView photoView;

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.downloadFailed)
    TextView downloadFailed;

    @InjectExtra
    String imageUrl;

    public static void startActivity(Activity activity, String imageUrl) {
        Intent intent = Henson.with(activity)
                .gotoZoomablePictureActivity()
                .imageUrl(imageUrl)
                .build();

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomable_picture);
        ButterKnife.bind(this);
        Dart.inject(this);

        progressBar.setVisibility(View.VISIBLE);
        downloadFailed.setVisibility(View.GONE);
        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        downloadFailed.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(photoView);
    }
}
