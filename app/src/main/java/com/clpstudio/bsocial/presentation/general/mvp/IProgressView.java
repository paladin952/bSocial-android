package com.clpstudio.bsocial.presentation.general.mvp;

/**
 * Base view that has show/hide progress behavior
 */
public interface IProgressView extends IBaseMvpPresenter.View {

    void showProgress();

    void hideProgress();

}
