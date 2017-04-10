package com.clpstudio.bsocial.presentation.general.mvp;

import android.support.annotation.NonNull;

public interface IBaseMvpPresenter<V> {

    void bindView(@NonNull V view);

    void unbindView();

    /**
     * Represents the View component inside the Model View Presenter pattern. This interface must be
     * used as base interface for every View interface declared.
     */
    public interface View {

    }
}
