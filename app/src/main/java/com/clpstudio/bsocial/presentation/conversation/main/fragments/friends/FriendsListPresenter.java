package com.clpstudio.bsocial.presentation.conversation.main.fragments.friends;

import com.clpstudio.bsocial.bussiness.service.ConversationService;
import com.clpstudio.bsocial.data.models.ui.FriendsListItemModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IProgressView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class FriendsListPresenter extends BaseMvpPresenter<FriendsListPresenter.View> {

    @Inject
    ConversationService conversationService;

    @Inject
    public FriendsListPresenter() {
    }

    public void loadData() {
        conversationService.getFriends()
                .subscribe(friendsListItemModels -> {
                    view().showData(friendsListItemModels);
                }, err -> {

                });
    }

    public interface View extends IProgressView {

        void showData(List<FriendsListItemModel> data);

    }

}
