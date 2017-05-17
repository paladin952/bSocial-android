package com.clpstudio.bsocial.presentation.conversation.main.fragments.friends;

import android.content.Context;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.service.ConversationService;
import com.clpstudio.bsocial.bussiness.service.DatabaseService;
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
    DatabaseService databaseService;
    @Inject
    Context context;

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

    public void onAddFriendsClick() {
        view().showAddFriendDialog();
    }

    public void addFriendIfExists(String email) {
        databaseService
                .addFriendToUsersList(email)
                .subscribe(() -> {
                    String successText = context.getString(R.string.friend_added_successfuly);
                    view().hideAddFriendDialog();
                    view().showToast(successText);
                    view().append(new FriendsListItemModel(email));
                }, err -> {
                    String successText = context.getString(R.string.friend_not_added);
                    view().showToast(successText);
                });
    }

    public interface View extends IProgressView {

        void showData(List<FriendsListItemModel> data);

        void append(FriendsListItemModel data);

        void showAddFriendDialog();

        void hideAddFriendDialog();

        void showToast(String message);

    }

}
