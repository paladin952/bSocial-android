package com.clpstudio.bsocial.presentation.conversation.main.fragments.friends;

import android.content.Context;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.Mapper;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IProgressView;
import com.clpstudio.domain.usecases.ConversationUseCases;
import com.clpstudio.domain.usecases.UserUseCases;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class FriendsListPresenter extends BaseMvpPresenter<FriendsListPresenter.View> {

    @Inject
    ConversationUseCases conversationUseCases;
    @Inject
    UserUseCases userUseCases;
    @Inject
    Context context;

    @Inject
    public FriendsListPresenter() {
    }

    public void loadData() {
        userUseCases.getFriends()
                .map(Mapper::toRegisteredUserViewModels)
                .subscribe(friendsListItemModels -> {
                    view().showData(friendsListItemModels);
                }, err -> {

                });
    }

    public void onAddFriendsClick() {
        view().showAddFriendDialog();
    }

    public void addFriendIfExists(String email) {
        userUseCases
                .addFriend(email)
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

        void showData(List<RegisteredUserViewModel> data);

        void refreshData();

        void showAddFriendDialog();

        void hideAddFriendDialog();

        void showToast(String message);

    }

}
