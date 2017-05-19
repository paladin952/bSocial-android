package com.clpstudio.bsocial.presentation.conversation.main.fragments.friends;

import android.content.Context;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.service.ConversationService;
import com.clpstudio.bsocial.bussiness.service.DatabaseService;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IProgressView;

import java.util.ArrayList;
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
        databaseService.getFriends()
                .subscribe(friendsListItemModels -> {
                    List<RegisteredUser> friendsUiList = new ArrayList<>();
                    view().showData(friendsUiList);
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
                    view().refreshData();
                }, err -> {
                    String successText = context.getString(R.string.friend_not_added);
                    view().showToast(successText);
                });
    }

    public interface View extends IProgressView {

        void showData(List<RegisteredUser> data);

        void refreshData();

        void showAddFriendDialog();

        void hideAddFriendDialog();

        void showToast(String message);

    }

}
