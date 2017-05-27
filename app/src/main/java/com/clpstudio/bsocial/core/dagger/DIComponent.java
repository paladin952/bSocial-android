package com.clpstudio.bsocial.core.dagger;

import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.browser.BrowserViewActivity;
import com.clpstudio.bsocial.presentation.conversation.details.ConversationDetailActivity;
import com.clpstudio.bsocial.presentation.conversation.main.MainActivity;
import com.clpstudio.bsocial.presentation.conversation.main.fragments.conversations.ConversationsListFragment;
import com.clpstudio.bsocial.presentation.conversation.main.fragments.friends.FriendsListFragment;
import com.clpstudio.bsocial.presentation.profile.EditAvatarFragment;
import com.clpstudio.bsocial.presentation.profile.ProfilePageActivity;
import com.clpstudio.bsocial.presentation.splashscreen.SplashScreen;
import com.clpstudio.bsocial.presentation.login.LoginActivity;
import com.clpstudio.bsocial.presentation.register.RegisterActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                ApplicationModule.class,
                FirebaseModule.class,
                RetrofitModule.class
        }
)
@Singleton
public interface DIComponent {
    void inject(BSocialApplication inject);

    void inject(SplashScreen splashScreen);

    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity registerActivity);

    void inject(ConversationDetailActivity conversationDetailActivity);

    void inject(ProfilePageActivity activity);

    void inject(EditAvatarFragment editAvatarFragment);

    void inject(ConversationsListFragment conversationsListFragment);

    void inject(FriendsListFragment friendsListFragment);

    void inject(MainActivity activity);

    void inject(BrowserViewActivity activity);
}
